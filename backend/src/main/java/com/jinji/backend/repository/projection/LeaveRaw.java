package com.jinji.backend.repository.projection;

import com.jinji.backend.model.entity.Employee;
import com.jinji.backend.model.entity.LeaveRequest;
import com.jinji.backend.model.entity.LeaveType;
import com.jinji.backend.model.enums.PeriodType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LeaveRaw {
    private LocalDate startDate;
    private LocalDate endDate;

    private PeriodType startPeriod;
    private PeriodType endPeriod;

    private LocalDateTime createdAt;

    private Employee employee;
    private Employee createdBy;

    private LeaveType leaveType;

    private LeaveRequest leaveRequest;

    private BigDecimal numberOfDays;

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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Employee getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Employee createdBy) {
        this.createdBy = createdBy;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public LeaveRequest getLeaveRequest() {
        return leaveRequest;
    }

    public void setLeaveRequest(LeaveRequest leaveRequest) {
        this.leaveRequest = leaveRequest;
    }

    public BigDecimal getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(BigDecimal numberOfDays) {
        this.numberOfDays = numberOfDays;
    }
}
