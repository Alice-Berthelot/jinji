package com.jinji.backend.model.dto;

import com.jinji.backend.model.entity.LeaveRequest;
import com.jinji.backend.model.enums.LeaveRequestStatus;
import com.jinji.backend.model.enums.PeriodType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class LeaveDTO {

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private PeriodType startPeriod;
    private PeriodType endPeriod;
    private LeaveRequestStatus status;
    private String leaveTypeLabel;
    private LocalDateTime createdAt;
    private BigDecimal numberOfDays;
    private LeaveRequest leaveRequest; // TODO: implémenter

}