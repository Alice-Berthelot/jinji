package com.jinji.backend.service.business;

import com.jinji.backend.exception.BadRequestException;
import com.jinji.backend.model.enums.AnnualLeaveDayType;
import com.jinji.backend.model.enums.PeriodType;
import com.jinji.backend.service.crud.HrPolicyService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class LeaveCalculationService {

    private final CalendarService calendarService;
    private final HrPolicyService hrPolicyService;

    public LeaveCalculationService(CalendarService calendarService, HrPolicyService hrPolicyService) {
        this.calendarService = calendarService;
        this.hrPolicyService = hrPolicyService;
    }

    public BigDecimal calculateLeaveDays(
            LocalDate startDate,
            LocalDate endDate,
            PeriodType startPeriod,
            PeriodType endPeriod
    ) {

        if (startDate == null || endDate == null) {
            return BigDecimal.ZERO;
        }

        AnnualLeaveDayType dayType = hrPolicyService.getAnnualLeaveDayType();
        LocalDate solidarityDay = hrPolicyService.getEffectiveSolidarityDay();

        BigDecimal total = BigDecimal.ZERO;

        for (
                LocalDate current = startDate;
                !current.isAfter(endDate);
                current = current.plusDays(1)
        ) {

            boolean shouldCount = switch (dayType) {

                case FR_JOURS_OUVRES ->
                        calendarService.isWorkingDay(current);

                case FR_JOURS_OUVRABLES ->
                        calendarService.isBusinessDay(current);
            };

            if (shouldCount) {
                total = total.add(BigDecimal.ONE);
            }

            boolean isSolidarityDay =
                    calendarService.isSolidarityDay(current, solidarityDay);

            if (isSolidarityDay) {
                total = total.add(BigDecimal.ONE);
            }
        }

        // Same day
        if (startDate.equals(endDate)) {

            // Invalid
            if (startPeriod == PeriodType.PM
                    && endPeriod == PeriodType.AM) {

                throw new BadRequestException(
                        "End period cannot be before start period"
                );
            }

            // Day that should not be counted
            boolean isSolidarityDay =
                    calendarService.isSolidarityDay(startDate, solidarityDay);

            if (total.compareTo(BigDecimal.ZERO) == 0) {
                return BigDecimal.ZERO;
            }

            // Half-day
            if (startPeriod == endPeriod) {
                return BigDecimal.valueOf(0.5);
            }

            // Full day
            return BigDecimal.ONE;

        } else {

            // First day: afternoon only
            if (startPeriod == PeriodType.PM) {
                total = total.subtract(BigDecimal.valueOf(0.5));
            }

            // Last day: morning only
            if (endPeriod == PeriodType.AM) {
                total = total.subtract(BigDecimal.valueOf(0.5));
            }
        }

        if (total.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }

        return total;
    }
}