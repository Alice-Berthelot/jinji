package com.jinji.backend.model.dto;

import com.jinji.backend.model.enums.LeaveRequestStatus;
import com.jinji.backend.model.enums.PeriodType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LeaveRequestDTO {

    private Long id;
    private LocalDateTime createdAt;
    private LocalDate startDate;
    private LocalDate endDate;
    private PeriodType startPeriod;
    private PeriodType endPeriod;
    private LeaveRequestStatus status;
    private String employeeComment;
    private String leaveTypeLabel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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

    public LeaveRequestStatus getStatus() {
        return status;
    }

    public void setStatus(LeaveRequestStatus status) {
        this.status = status;
    }

    public String getEmployeeComment() {
        return employeeComment;
    }

    public void setEmployeeComment(String employeeComment) {
        this.employeeComment = employeeComment;
    }

    public String getLeaveTypeLabel() {
        return leaveTypeLabel;
    }

    public void setLeaveTypeLabel(String leaveTypeLabel) {
        this.leaveTypeLabel = leaveTypeLabel;
    }
}