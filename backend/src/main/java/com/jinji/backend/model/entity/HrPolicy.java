package com.jinji.backend.model.entity;

import com.jinji.backend.model.enums.AnnualLeaveDayType;
import com.jinji.backend.model.enums.LeaveValidationProcess;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "hr_policy", schema = "configuration")
public class HrPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_validation", nullable = false)
    private LeaveValidationProcess leaveValidation;

    @Column(name = "allow_unpaid_leave", nullable = false)
    private Boolean allowUnpaidLeave;

    @Enumerated(EnumType.STRING)
    @Column(name = "annual_leave_day_type", nullable = false)
    private AnnualLeaveDayType annualLeaveDayType;

    @Column(name = "solidarity_day")
    private LocalDate solidarityDay;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LeaveValidationProcess getLeaveValidation() {
        return leaveValidation;
    }

    public void setLeaveValidation(LeaveValidationProcess leaveValidation) {
        this.leaveValidation = leaveValidation;
    }

    public Boolean getAllowUnpaidLeave() {
        return allowUnpaidLeave;
    }

    public void setAllowUnpaidLeave(Boolean allowUnpaidLeave) {
        this.allowUnpaidLeave = allowUnpaidLeave;
    }

    public AnnualLeaveDayType getAnnualLeaveDayType() {
        return annualLeaveDayType;
    }

    public void setAnnualLeaveDayType(AnnualLeaveDayType annualLeaveDayType) {
        this.annualLeaveDayType = annualLeaveDayType;
    }

    public LocalDate getSolidarityDay() {
        return solidarityDay;
    }

    public void setSolidarityDay(LocalDate solidarityDay) {
        this.solidarityDay = solidarityDay;
    }
}