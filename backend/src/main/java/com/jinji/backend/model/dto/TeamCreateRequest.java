package com.jinji.backend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public class TeamCreateRequest {

    @NotBlank
    private String label;

    @NotNull
    private Long managerId;

    private Set<Long> employeeIds;

    public @NotBlank String getLabel() {
        return label;
    }

    public void setLabel(@NotBlank String label) {
        this.label = label;
    }

    public @NotNull Long getManagerId() {
        return managerId;
    }

    public void setManagerId(@NotNull Long managerId) {
        this.managerId = managerId;
    }

    public Set<Long> getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(Set<Long> employeeIds) {
        this.employeeIds = employeeIds;
    }
}