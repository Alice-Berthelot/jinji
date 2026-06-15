package com.jinji.backend.model.dto.response;

import com.jinji.backend.model.enums.EmployeeStatus;

import java.time.LocalDate;
import java.util.List;

public record EmployeeDetailsDTO(
        Long id,
        String employeeNumber,
        String surname,
        String firstName,
        String email,
        String phoneNumber,
        LocalDate seniorityDate,
        EmployeeStatus status,
        String departmentName,
        List<String> teams
) {
}