package com.jinji.backend.model.dto.request;

import java.math.BigDecimal;

public record AdjustAcquiredDaysRequest(
        BigDecimal newAcquiredDays
) {}