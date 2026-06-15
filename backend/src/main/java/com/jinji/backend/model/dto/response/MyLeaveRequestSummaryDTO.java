package com.jinji.backend.model.dto.response;

import com.jinji.backend.model.enums.LeaveRequestStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record MyLeaveRequestSummaryDTO(
        Long id,
        String leaveTypeLabel,
        LocalDate startDate,
        LocalDate endDate,
        LeaveRequestStatus status,
        String statusLabel,
        LocalDateTime createdAt
) {}