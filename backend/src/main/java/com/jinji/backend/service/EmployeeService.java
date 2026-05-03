package com.jinji.backend.service;

import com.jinji.backend.model.dto.EmployeeCreateRequest;
import com.jinji.backend.model.dto.EmployeeDTO;
import com.jinji.backend.model.entity.Department;
import com.jinji.backend.model.entity.Employee;
import com.jinji.backend.model.entity.User;
import com.jinji.backend.model.enums.RoleEnum;
import com.jinji.backend.repository.DepartmentRepository;
import com.jinji.backend.repository.EmployeeRepository;
import com.jinji.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public EmployeeService(EmployeeRepository employeeRepository,
                           DepartmentRepository departmentRepository,
                           UserRepository userRepository,
                           UserService userService) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public EmployeeDTO getCurrentEmployee(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Employee employee = user.getEmployee();

        if (employee == null) {
            throw new RuntimeException("No employee linked to this user");
        }

        return mapToDto(employee);
    }

    private EmployeeDTO mapToDto(Employee e) {
        EmployeeDTO dto = new EmployeeDTO();

        dto.setEmployeeNumber(e.getEmployeeNumber());
        dto.setSurname(e.getSurname());
        dto.setFirstName(e.getFirstName());
        dto.setEmail(e.getEmail());
        dto.setPhoneNumber(e.getPhoneNumber());
        dto.setSeniorityDate(e.getSeniorityDate());
        dto.setDepartmentCode(
                e.getDepartment() != null ? e.getDepartment().getCode() : null
        );

        return dto;
    }

    public String createEmployee(EmployeeCreateRequest request) {

        Department department = departmentRepository.findByCode(request.getDepartmentCode())
                .orElseThrow(() -> new RuntimeException(
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

        if (Boolean.TRUE.equals(request.getCreateUser())) {

            if (request.getPassword() == null || request.getPassword().isBlank()) {
                throw new RuntimeException("Password is required to create a user");
            }

            Set<RoleEnum> roles =  mapToRoleEnums(request.getRoles());

            userService.createUser(
                    savedEmployee.getEmail(), // username = email
                    request.getPassword(),
                    roles
            );
        }

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
}

