package com.jinji.backend.controller;

import com.jinji.backend.model.dto.EmployeeCreateRequest;
import com.jinji.backend.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
}