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
    private String statusLabel;
    private LocalDateTime createdAt;
    private String employeeFirstName;
    private String employeeSurname;
    private Boolean hasHrReview;
    private Boolean hasManagerReview;

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

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getEmployeeFirstName() {
        return employeeFirstName;
    }

    public void setEmployeeFirstName(String employeeFirstName) {
        this.employeeFirstName = employeeFirstName;
    }

    public String getEmployeeSurname() {
        return employeeSurname;
    }

    public void setEmployeeSurname(String employeeSurname) {
        this.employeeSurname = employeeSurname;
    }

    public Boolean getHasHrReview() {
        return hasHrReview;
    }

    public void setHasHrReview(Boolean hasHrReview) {
        this.hasHrReview = hasHrReview;
    }

    public Boolean getHasManagerReview() {
        return hasManagerReview;
    }

    public void setHasManagerReview(Boolean hasManagerReview) {
        this.hasManagerReview = hasManagerReview;
    }
}