package com.jinji.backend.model.dto;

import java.time.LocalDate;

public record MyLeaveCalendarDTO(
    LeaveTypeDTO leaveType,
    LocalDate startDate,
    LocalDate endDate,
    Long leaveId) {}