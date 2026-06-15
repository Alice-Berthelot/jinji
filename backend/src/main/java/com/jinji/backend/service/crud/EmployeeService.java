package com.jinji.backend.service.crud;

import com.jinji.backend.exception.BadRequestException;
import com.jinji.backend.exception.ForbiddenException;
import com.jinji.backend.exception.ResourceNotFoundException;
import com.jinji.backend.mapper.EmployeeMapper;
import com.jinji.backend.model.dto.request.EmployeeCreateRequest;
import com.jinji.backend.model.dto.response.*;
import com.jinji.backend.model.entity.Department;
import com.jinji.backend.model.entity.Employee;
import com.jinji.backend.model.entity.Team;
import com.jinji.backend.model.entity.User;
import com.jinji.backend.model.enums.EmployeePageView;
import com.jinji.backend.model.enums.EmployeeStatus;
import com.jinji.backend.model.enums.RoleEnum;
import com.jinji.backend.model.projection.EmployeeTeamProjection;
import com.jinji.backend.repository.DepartmentRepository;
import com.jinji.backend.repository.EmployeeRepository;
import com.jinji.backend.repository.TeamRepository;
import com.jinji.backend.repository.UserRepository;
import com.jinji.backend.service.business.PermissionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final PermissionService permissionService;
    private final EmployeeMapper employeeMapper;

    public EmployeeService(TeamRepository teamRepository, EmployeeRepository employeeRepository,
                           DepartmentRepository departmentRepository,
                           UserRepository userRepository,
                           UserService userService
                           , LeaveBalanceService leaveBalanceService, PermissionService permissionService, EmployeeMapper employeeMapper
    ) {
        this.teamRepository = teamRepository;
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.leaveBalanceService = leaveBalanceService;
        this.userService = userService;
        this.permissionService = permissionService;
        this.employeeMapper = employeeMapper;
    }

    public Employee getCurrentEmployee() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findEmployeeByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("No employee linked to this user"));
    }

    public EmployeeMeDTO getMe() {

        Employee currentEmployee = getCurrentEmployee();

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

        if (!permissionService.canViewEmployeeDetails(pageType, currentUser, authEmployee, targetEmployee)) {
            throw new ForbiddenException("User not authorized to see Employee details");
        }

        Employee employee = employeeRepository.findByIdWithDetails(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found with id: " + employeeId
                        ));

        return pageType == EmployeePageView.MANAGER
                ? employeeMapper.toManagerDetailsDto(employee)
                : employeeMapper.toDetailsDto(employee);
    }


    public EmployeeFullNameDTO getEmployeeFullNameById(Long employeeId) {
        return employeeRepository.findEmployeeFullNameById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id " + employeeId + " not found"));
    }

    public EmployeeFullNameDTO getMyFullName(String username) {
        return employeeRepository.findEmployeeNameByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with username " + username + " not found"));
    }

    @Transactional(readOnly = true)
    public Page<EmployeeTableDTO> getEmployeesForTable(
            String search,
            Pageable pageable
    ) {
        Page<EmployeeProfileDTO> employeePage =
                employeeRepository.findEmployeesForTable(search, pageable);

        List<Long> employeeIds = employeePage.getContent()
                .stream()
                .map(EmployeeProfileDTO::id)
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

        return employeePage.map(e -> new EmployeeTableDTO(
                e.id(),
                e.employeeNumber(),
                e.surname(),
                e.firstName(),
                e.email(),
                e.phoneNumber(),
                e.seniorityDate(),
                e.status(),
                e.departmentName(),
                teamsByEmployee.getOrDefault(e.id(), List.of())
        ));
    }

    @Transactional(readOnly = true)
    public Page<ManagerEmployeeTableDTO> getEmployeesForManager(Pageable pageable) {
        Long managerId = getCurrentEmployee().getId();
        return employeeRepository.findEmployeesManagedBy(managerId, pageable);
    }

    @Transactional
    public EmployeeCreatedDTO createEmployee(EmployeeCreateRequest request) {
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
        employee.setStatus(
                request.getStatus() != null ? request.getStatus() : EmployeeStatus.INTERNAL
        );
        employee.setDepartment(department);

        Employee savedEmployee = employeeRepository.save(employee);

        Set<Long> memberTeamIds = Optional.ofNullable(request.getMemberTeamIds())
                .orElse(Set.of());

        Set<Long> managerTeamIds = Optional.ofNullable(request.getManagerTeamIds())
                .orElse(Set.of());

        Set<Long> effectiveMemberTeamIds = new HashSet<>(memberTeamIds);
        effectiveMemberTeamIds.removeAll(managerTeamIds);

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

                // CASE 2 : member (manager excluded)
                if (effectiveMemberTeamIds.contains(teamId)) {
                    team.addEmployee(savedEmployee);
                }
            }

            teamRepository.saveAll(teams);
        }

        if (request.getStatus() == EmployeeStatus.EXTERNAL
                && Boolean.TRUE.equals(request.getCreateUser())) {
            throw new BadRequestException(
                    "External employees cannot have a user account"
            );
        }

        if (Boolean.TRUE.equals(request.getCreateUser())) {

            if (request.getPassword() == null || request.getPassword().isBlank()) {
                throw new BadRequestException("Password is required to create a user");
            }

            Set<RoleEnum> roles = new HashSet<>();

            roles.add(RoleEnum.EMPLOYEE);

            if (!managerTeamIds.isEmpty()) {
                roles.add(RoleEnum.MANAGER);
            }

            if ("RH".equalsIgnoreCase(department.getCode())) {
                roles.add(RoleEnum.HR);
            }

            userService.createUser(
                    savedEmployee.getEmail(), // username = email
                    request.getPassword(),
                    roles,
                    savedEmployee
            );
        }

        if (savedEmployee.getStatus() == EmployeeStatus.INTERNAL) {
            leaveBalanceService.createLeaveBalance(savedEmployee);
        }

        return employeeMapper.toCreatedDto(savedEmployee);   }

    // HELPERS

    private String normalizeName(String value) {
        if (value == null) return null;

        value = value.trim().toLowerCase();

        return value.substring(0, 1).toUpperCase() + value.substring(1);
    }

    private String normalizeEmail(String value) {
        if (value == null) return null;

        return value.trim().toLowerCase();
    }

}

