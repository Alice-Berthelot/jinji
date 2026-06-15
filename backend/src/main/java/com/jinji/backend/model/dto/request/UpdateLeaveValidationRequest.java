package com.jinji.backend.model.dto.request;

import com.jinji.backend.model.enums.LeaveValidationProcess;

public class UpdateLeaveValidationRequest {
    private LeaveValidationProcess leaveValidation;

    public LeaveValidationProcess getLeaveValidation() {
        return leaveValidation;
    }

    public void setLeaveValidation(LeaveValidationProcess leaveValidation) {
        this.leaveValidation = leaveValidation;
    }
}