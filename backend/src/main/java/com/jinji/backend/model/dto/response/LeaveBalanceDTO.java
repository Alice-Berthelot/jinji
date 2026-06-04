package com.jinji.backend.model.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LeaveBalanceDTO(
        Long id,
        String label,
        LocalDate acquisitionStartDate,
        LocalDate acquisitionEndDate,
        BigDecimal acquiredDays,
        BigDecimal takenDays,
        BigDecimal remainingDays,
        String leaveType
) {}