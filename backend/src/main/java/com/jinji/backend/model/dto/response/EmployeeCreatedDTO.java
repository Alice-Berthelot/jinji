package com.jinji.backend.model.dto.response;

public record EmployeeCreatedDTO(
    Long id,
    String firstName,
    String surname
) {}