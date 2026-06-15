package com.jinji.backend.model.dto.response;

import com.jinji.backend.model.enums.LeaveStatus;
import com.jinji.backend.model.enums.PeriodType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record LeaveDTO(
        Long id,
        Long employeeId,
        String employeeFullName,
        String leaveTypeLabel,
        LocalDate startDate,
        LocalDate endDate,
        PeriodType startPeriod,
        PeriodType endPeriod,
        LeaveStatus status,
        Long leaveRequestId,
        LocalDateTime createdAt,
        Long creatorId,
        String creatorFullName
) {}