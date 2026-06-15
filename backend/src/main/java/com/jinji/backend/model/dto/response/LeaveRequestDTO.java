package com.jinji.backend.model.dto.response;

import com.jinji.backend.model.dto.LeaveRequestReviewDTO;
import com.jinji.backend.model.enums.LeaveRequestStatus;
import com.jinji.backend.model.enums.LeaveRequestWorkflowStatus;
import com.jinji.backend.model.enums.PeriodType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class LeaveRequestDTO {

    private Long leaveRequestId;
    private Long employeeId;
    private String employeeFirstName;
    private String employeeSurname;
    private LocalDateTime createdAt;
    private LocalDate startDate;
    private LocalDate endDate;
    private PeriodType startPeriod;
    private PeriodType endPeriod;
    private LeaveRequestStatus status;
    private LeaveRequestWorkflowStatus workflowStatus;
    private String statusLabel;
    private String employeeComment;
    private String leaveTypeLabel;
    private List<LeaveRequestReviewDTO> reviews;
    private BigDecimal numberOfDays;

    public Long getLeaveRequestId() {
        return leaveRequestId;
    }

    public void setLeaveRequestId(Long leaveRequestId) {
        this.leaveRequestId = leaveRequestId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
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

    public LeaveRequestWorkflowStatus getWorkflowStatus() {
        return workflowStatus;
    }

    public void setWorkflowStatus(LeaveRequestWorkflowStatus workflowStatus) {
        this.workflowStatus = workflowStatus;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
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

    public List<LeaveRequestReviewDTO> getReviews() {
        return reviews;
    }

    public void setReviews(List<LeaveRequestReviewDTO> reviews) {
        this.reviews = reviews;
    }

    public BigDecimal getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(BigDecimal numberOfDays) {
        this.numberOfDays = numberOfDays;
    }
}