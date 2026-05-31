package com.jinji.backend.model.dto;

import java.time.LocalDate;

public record EmployeePageDTO(
        Long id,
        String employeeNumber,
        String surname,
        String firstName,
        String email,
        String phoneNumber,
        LocalDate seniorityDate,
        String departmentName
) {}