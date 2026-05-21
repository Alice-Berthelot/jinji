package com.jinji.backend.model.dto;

import com.jinji.backend.model.enums.LeaveStatus;
import com.jinji.backend.model.enums.PeriodType;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LeaveCreateRequest {

    private Long employeeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private PeriodType startPeriod;
    private PeriodType endPeriod;
    private LocalDateTime createdAt;
    private String leaveTypeCode;

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public PeriodType getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(PeriodType startPeriod) {
        this.startPeriod = startPeriod;
    }

    public PeriodType getEndPeriod() {
        return endPeriod;
    }

    public void setEndPeriod(PeriodType endPeriod) {
        this.endPeriod = endPeriod;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getLeaveTypeCode() {
        return leaveTypeCode;
    }

    public void setLeaveTypeCode(String leaveTypeCode) {
        this.leaveTypeCode = leaveTypeCode;
    }
}
