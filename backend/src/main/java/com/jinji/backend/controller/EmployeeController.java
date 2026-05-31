package com.jinji.backend.controller;

import com.jinji.backend.model.dto.*;
import com.jinji.backend.model.enums.EmployeePageView;
import com.jinji.backend.service.crud.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<String> createEmployee(
            @Valid @RequestBody EmployeeCreateRequest request) {

        return ResponseEntity.ok(employeeService.createEmployee(request));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<Page<EmployeeTableDTO>> getEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Order.asc("surname"),
                        Sort.Order.asc("firstName")
                )
        );

        return ResponseEntity.ok(
                employeeService.getEmployeesForTable(search, pageable)
        );
    }

    @GetMapping("/{employeeId}")
    @PreAuthorize("hasRole('HR') or hasRole('MANAGER')")
    public EmployeeDetailsDTO getEmployeeById(
            @PathVariable Long employeeId,
            @RequestParam EmployeePageView pageType
    ) {
        return employeeService.getEmployeeById(employeeId, pageType);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public EmployeeMeDTO getMe(@AuthenticationPrincipal UserDetails userDetails) {
        return employeeService.getMe(userDetails.getUsername());
    }

    @GetMapping("/me/fullname")
    @PreAuthorize("isAuthenticated()")
    public EmployeeFullNameDTO getMyFullName(@AuthenticationPrincipal UserDetails userDetails) {
        return employeeService.getMyFullName(userDetails.getUsername());
    }

    @GetMapping("/{id}/fullname")
    @PreAuthorize("hasAnyRole('HR', 'MANAGER')")
    public EmployeeFullNameDTO getEmployeeFullNameById(@PathVariable Long id) {
        return employeeService.getEmployeeFullNameById(id);
    }
}