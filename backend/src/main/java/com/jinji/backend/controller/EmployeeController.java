package com.jinji.backend.controller;

import com.jinji.backend.model.dto.request.EmployeeCreateRequest;
import com.jinji.backend.model.dto.response.*;
import com.jinji.backend.model.enums.EmployeePageView;
import com.jinji.backend.service.crud.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<EmployeeCreatedDTO> createEmployee(
            @Valid @RequestBody EmployeeCreateRequest request) {

        EmployeeCreatedDTO created = employeeService.createEmployee(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<Page<EmployeeTableDTO>> getAllEmployees(
            Pageable pageable,
            @RequestParam(required = false) String search
    ) {
        return ResponseEntity.ok(
                employeeService.getEmployeesForTable(search, pageable)
        );
    }

    @GetMapping("/team")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Page<ManagerEmployeeTableDTO>> getEmployeesForManager(
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                employeeService.getEmployeesForManager(pageable)
        );
    }

    @GetMapping("/{employeeId}")
    @PreAuthorize("hasRole('HR') or hasRole('MANAGER')")
    public ResponseEntity<EmployeeDetailsDTO> getEmployeeById(
            @PathVariable Long employeeId,
            @RequestParam EmployeePageView pageType
    ) {
        return ResponseEntity.ok(employeeService.getEmployeeById(employeeId, pageType));
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EmployeeMeDTO> getMe() {
        return ResponseEntity.ok(employeeService.getMe());
    }

    @GetMapping("/me/fullname")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EmployeeFullNameDTO> getMyFullName(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(employeeService.getMyFullName(userDetails.getUsername()));
    }

    @GetMapping("/{id}/fullname")
    @PreAuthorize("hasAnyRole('HR', 'MANAGER')")
    public ResponseEntity<EmployeeFullNameDTO> getEmployeeFullNameById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeFullNameById(id));
    }
}