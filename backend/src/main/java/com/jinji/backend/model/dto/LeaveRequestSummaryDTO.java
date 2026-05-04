package com.jinji.backend.model.dto;

import com.jinji.backend.model.enums.LeaveRequestStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LeaveRequestSummaryDTO {

    private Long id;
    private String leaveTypeLabel;
    private LocalDate startDate;
    private LocalDate endDate;
    private LeaveRequestStatus status;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLeaveTypeLabel() {
        return leaveTypeLabel;
    }

    public void setLeaveTypeLabel(String leaveTypeLabel) {
        this.leaveTypeLabel = leaveTypeLabel;
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

    public LeaveRequestStatus getStatus() {
        return status;
    }

    public void setStatus(LeaveRequestStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}