package com.jinji.backend.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LeaveBalanceDTO {

    private String label;
    private LocalDate acquisitionStartDate;
    private LocalDate acquisitionEndDate;
    private BigDecimal acquiredDays;
    private BigDecimal takenDays;
    private BigDecimal remainingDays;
    private String leaveType;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public LocalDate getAcquisitionStartDate() {
        return acquisitionStartDate;
    }

    public void setAcquisitionStartDate(LocalDate acquisitionStartDate) {
        this.acquisitionStartDate = acquisitionStartDate;
    }

    public LocalDate getAcquisitionEndDate() {
        return acquisitionEndDate;
    }

    public void setAcquisitionEndDate(LocalDate acquisitionEndDate) {
        this.acquisitionEndDate = acquisitionEndDate;
    }

    public BigDecimal getAcquiredDays() {
        return acquiredDays;
    }

    public void setAcquiredDays(BigDecimal acquiredDays) {
        this.acquiredDays = acquiredDays;
    }

    public BigDecimal getTakenDays() {
        return takenDays;
    }

    public void setTakenDays(BigDecimal takenDays) {
        this.takenDays = takenDays;
    }

    public BigDecimal getRemainingDays() {
        return remainingDays;
    }

    public void setRemainingDays(BigDecimal remainingDays) {
        this.remainingDays = remainingDays;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }
}