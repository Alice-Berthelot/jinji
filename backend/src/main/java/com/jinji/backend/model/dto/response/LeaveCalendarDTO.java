package com.jinji.backend.model.dto.response;

import com.jinji.backend.model.enums.LeaveStatus;

import java.time.LocalDate;

public record LeaveCalendarDTO(
    Long employeeId,
    String firstName,
    String surname,
    LeaveTypeDTO leaveType,
    LocalDate startDate,
    LocalDate endDate,
    Long leaveId,
    LeaveStatus status) {}