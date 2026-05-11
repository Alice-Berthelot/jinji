package com.jinji.backend.model.entity;

import com.jinji.backend.model.enums.LeaveValidationProcess;
import jakarta.persistence.*;

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
}