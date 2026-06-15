package com.jinji.backend.model.dto.response;

import com.jinji.backend.model.enums.AnnualLeaveAccrualPeriod;
import com.jinji.backend.model.enums.AnnualLeaveDayType;
import com.jinji.backend.model.enums.LeaveValidationProcess;

import java.time.LocalDate;

public record HrPolicyDTO(
        Long id,
        LeaveValidationProcess leaveValidation,
        Boolean allowUnpaidLeave,
        AnnualLeaveDayType annualLeaveDayType,
        LocalDate solidarityDay,
        AnnualLeaveAccrualPeriod annualLeaveAccrualPeriod,
        Boolean allowAnnualLeaveCarryover
) {}