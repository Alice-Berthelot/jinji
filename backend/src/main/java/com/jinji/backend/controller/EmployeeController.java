package com.jinji.backend.controller;

import com.jinji.backend.model.dto.*;
import com.jinji.backend.service.crud.EmployeeService;
import jakarta.validation.Valid;
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

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public EmployeeMeDTO getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return employeeService.getEmployeeMe(userDetails.getUsername());
    }

    @GetMapping("/me/fullname")
    @PreAuthorize("isAuthenticated()")
    public EmployeeFullNameDTO getMyFullName(@AuthenticationPrincipal UserDetails userDetails) {
        return employeeService.getMyFullName(userDetails.getUsername());
    }

    @GetMapping("/{id}/fullname")
    @PreAuthorize("hasRole('HR') or hasRole('MANAGER')")
    public EmployeeFullNameDTO getEmployeeFullNameById(@PathVariable Long id) {
        return employeeService.getEmployeeFullNameById(id);
    }
}