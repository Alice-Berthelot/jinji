package com.jinji.backend.model.dto;

import java.time.LocalDate;
import java.util.List;

public record EmployeeTableDTO(
        Long id,
        String employeeNumber,
        String surname,
        String firstName,
        String email,
        String phoneNumber,
        LocalDate seniorityDate,
        String departmentName,
        List<String> teams
) {}