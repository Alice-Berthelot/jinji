package com.jinji.backend.model.projection;

import java.time.LocalDate;

public interface LeaveCalendarProjection {

    Long getEmployeeId();
    String getFirstName();
    String getSurname();

    String getLeaveTypeCode();
    String getLeaveTypeLabel();

    LocalDate getStartDate();
    LocalDate getEndDate();
    Long getLeaveId();
}