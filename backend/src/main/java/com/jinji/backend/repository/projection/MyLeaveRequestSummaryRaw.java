package com.jinji.backend.repository.projection;

import com.jinji.backend.model.enums.LeaveRequestStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface MyLeaveRequestSummaryRaw {
    Long getId();
    String getLeaveTypeLabel();
    LocalDate getStartDate();
    LocalDate getEndDate();
    LeaveRequestStatus getStatus();
    LocalDateTime getCreatedAt();
    BigDecimal getNumberOfDays();
}
