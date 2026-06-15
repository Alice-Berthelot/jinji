package com.jinji.backend.model.dto.response;

import com.jinji.backend.model.enums.EmployeeStatus;

import java.time.LocalDate;

public record ManagerEmployeeTableDTO(
        Long id,
        String surname,
        String firstName,
        String email,
        LocalDate seniorityDate,
        EmployeeStatus status,
        String departmentName
) {}