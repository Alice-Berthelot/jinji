package com.jinji.backend.service.crud;

import com.jinji.backend.exception.BadRequestException;
import com.jinji.backend.exception.ForbiddenException;
import com.jinji.backend.exception.ResourceNotFoundException;
import com.jinji.backend.mapper.EmployeeMapper;
import com.jinji.backend.model.dto.*;
import com.jinji.backend.model.entity.Department;
import com.jinji.backend.model.entity.Employee;
import com.jinji.backend.model.entity.Team;
import com.jinji.backend.model.entity.User;
import com.jinji.backend.model.enums.EmployeePageView;
import com.jinji.backend.model.enums.RoleEnum;
import com.jinji.backend.model.projection.EmployeeTeamProjection;
import com.jinji.backend.repository.DepartmentRepository;
import com.jinji.backend.repository.EmployeeRepository;
import com.jinji.backend.repository.TeamRepository;
import com.jinji.backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final TeamRepository teamRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final LeaveBalanceService leaveBalanceService;
    private final EmployeeMapper employeeMapper;

    public EmployeeService(TeamRepository teamRepository, EmployeeRepository employeeRepository,
                           DepartmentRepository departmentRepository,
                           UserRepository userRepository,
                           UserService userService
                           , LeaveBalanceService leaveBalanceService, EmployeeMapper employeeMapper
    ) {
        this.teamRepository = teamRepository;
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.leaveBalanceService = leaveBalanceService;
        this.userService = userService;
        this.employeeMapper = employeeMapper;
    }

    public Employee getCurrentEmployee(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        Employee employee = user.getEmployee();

        if (employee == null) {
            throw new ResourceNotFoundException("No employee linked to this user");
        }

        return employee;
    }

    public EmployeeFullNameDTO getEmployeeFullNameById(Long employeeId) {
        return employeeRepository.findEmployeeFullNameById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id " + employeeId + " not found"));
    }

    public EmployeeFullNameDTO getMyFullName(String username) {
        return employeeRepository.findEmployeeNameByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with username " + username + " not found"));
    }

    public EmployeeMeDTO getEmployeeMe(String username) {

        Employee currentEmployee = getCurrentEmployee(username);

        return employeeMapper.toMeDto(currentEmployee);
    }

    @Transactional(readOnly = true)
    public EmployeeDetailsDTO getEmployeeById(
            Long employeeId,
            EmployeePageView pageType
    ) {
        User currentUser = userService.getCurrentUser();
        Employee authEmployee = currentUser.getEmployee();
        Employee targetEmployee = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException(
                "No employee found with id " + employeeId
        ));

        if (!canAccess(pageType, currentUser, authEmployee, targetEmployee)) {
            throw new ForbiddenException("Not authorized");
        }

        Employee employee = employeeRepository.findByIdWithDetails(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found with id: " + employeeId
                        ));

        EmployeeDetailsDTO dto = employeeMapper.toDetailsDto(employee);

        if (pageType == EmployeePageView.MANAGER) {
            return new EmployeeDetailsDTO(
                    dto.id(),
                    null,
                    dto.surname(),
                    dto.firstName(),
                    dto.email(),
                    null,
                    dto.seniorityDate(),
                    dto.departmentName(),
                    null
            );
        }

        return dto;
    }

    private boolean canAccess(EmployeePageView pageType,
                              User currentUser,
                              Employee authEmployee,
                              Employee target) {

        return switch (pageType) {
            case HR -> currentUser.isHr();
            case MANAGER -> currentUser.isManager()
                    && isManagerOf(authEmployee, target);
        };
    }


    @Transactional
    public String createEmployee(EmployeeCreateRequest request) {

        Department department = departmentRepository.findByCode(request.getDepartmentCode())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found with code: " + request.getDepartmentCode()
                ));

        Employee employee = new Employee();
        employee.setEmployeeNumber(request.getEmployeeNumber());
        employee.setSurname(normalizeName(request.getSurname()));
        employee.setFirstName(normalizeName(request.getFirstName()));
        employee.setEmail(normalizeEmail(request.getEmail()));
        employee.setPhoneNumber(request.getPhoneNumber());
        employee.setSeniorityDate(request.getSeniorityDate());
        employee.setDepartment(department);

        Employee savedEmployee = employeeRepository.save(employee);

        Set<Long> memberTeamIds = Optional.ofNullable(request.getMemberTeamIds())
                .orElse(Set.of());

        Set<Long> managerTeamIds = Optional.ofNullable(request.getManagerTeamIds())
                .orElse(Set.of());

        // union (on charge toutes les teams en une seule query)
        Set<Long> allTeamIds = new HashSet<>();
        allTeamIds.addAll(memberTeamIds);
        allTeamIds.addAll(managerTeamIds);

        if (!allTeamIds.isEmpty()) {

            List<Team> teams = teamRepository.findAllByIdIn(allTeamIds);

            for (Team team : teams) {

                Long teamId = team.getId();

                // CASE 1 : manager
                if (managerTeamIds.contains(teamId)) {
                    team.setManager(savedEmployee);
                }

                // CASE 2 : member (non manager ou même si manager aussi — dépend de ta règle)
                if (memberTeamIds.contains(teamId)) {
                    team.addEmployee(savedEmployee);
                }
            }
        }





        if (Boolean.TRUE.equals(request.getCreateUser())) {

            if (request.getPassword() == null || request.getPassword().isBlank()) {
                throw new BadRequestException("Password is required to create a user");
            }

            Set<RoleEnum> roles =  mapToRoleEnums(request.getRoles());

            userService.createUser(
                    savedEmployee.getEmail(), // username = email
                    request.getPassword(),
                    roles,
                    savedEmployee
            );
        }

        leaveBalanceService.createLeaveBalance(savedEmployee);

        return "Successful registration for Employee " + savedEmployee.getFirstName() + " " + savedEmployee.getSurname();
    }

    private String normalizeName(String value) {
        if (value == null) return null;

        value = value.trim().toLowerCase();

        return value.substring(0, 1).toUpperCase() + value.substring(1);
    }

    private String normalizeEmail(String value) {
        if (value == null) return null;

        return value.trim().toLowerCase();
    }

    private Set<RoleEnum> mapToRoleEnums(Set<String> roles) {

        if (roles == null || roles.isEmpty()) {
            return Set.of(RoleEnum.EMPLOYEE);
        }

        return roles.stream()
                .map(role -> {
                    try {
                        return RoleEnum.valueOf(role.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException("Invalid role: " + role);
                    }
                })
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public Page<EmployeeTableDTO> getEmployeesForTable(
            String search,
            Pageable pageable
    ) {
        Page<EmployeePageDTO> employeePage =
                employeeRepository.findEmployeesForTable(search, pageable);

        List<Long> employeeIds = employeePage.getContent()
                .stream()
                .map(EmployeePageDTO::id)
                .toList();

        Map<Long, List<String>> teamsByEmployee = employeeIds.isEmpty()
                ? Map.of()
                : employeeRepository.findTeamNamesByEmployeeIds(employeeIds)
                .stream()
                .collect(Collectors.groupingBy(
                        EmployeeTeamProjection::getEmployeeId,
                        Collectors.mapping(
                                EmployeeTeamProjection::getLabel,
                                Collectors.toList()
                        )
                ));

        List<EmployeeTableDTO> content =
                employeePage.getContent()
                        .stream()
                        .map(e -> new EmployeeTableDTO(
                                e.id(),
                                e.employeeNumber(),
                                e.surname(),
                                e.firstName(),
                                e.email(),
                                e.phoneNumber(),
                                e.seniorityDate(),
                                e.departmentName(),
                                teamsByEmployee.getOrDefault(e.id(), List.of())
                        ))
                        .toList();

        return new PageImpl<>(
                content,
                pageable,
                employeePage.getTotalElements()
        );
    }

    private boolean isManagerOf(Employee manager, Employee employee) {
        return employee.getTeams().stream()
                .anyMatch(team -> team.getManager() != null
                        && team.getManager().getId().equals(manager.getId()));
    }
}

