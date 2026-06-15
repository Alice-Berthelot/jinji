package com.jinji.backend.model.dto.response;

import com.jinji.backend.model.enums.LeaveRequestStatus;
import com.jinji.backend.model.enums.LeaveRequestWorkflowStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record LeaveRequestSummaryDTO(
        Long id,
        String leaveTypeLabel,
        LocalDate startDate,
        LocalDate endDate,
        LeaveRequestStatus status,
        LeaveRequestWorkflowStatus workflowStatus,
        String statusLabel,
        LocalDateTime createdAt,
        String employeeFirstName,
        String employeeSurname,
        Boolean hasHrReview,
        Boolean hasManagerReview,
        BigDecimal numberOfDays
) {
}