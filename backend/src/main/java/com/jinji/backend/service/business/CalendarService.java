package com.jinji.backend.service.business;

import com.jinji.backend.model.entity.PublicHolidayFixed;
import com.jinji.backend.repository.PublicHolidayFixedRepository;
import com.jinji.backend.repository.PublicHolidayVariableRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
public class CalendarService {

    private final PublicHolidayFixedRepository fixedRepository;
    private final PublicHolidayVariableRepository variableRepository;

    public CalendarService(
            PublicHolidayFixedRepository fixedRepository,
            PublicHolidayVariableRepository variableRepository
    ) {
        this.fixedRepository = fixedRepository;
        this.variableRepository = variableRepository;
    }

    public boolean isHoliday(LocalDate date) {
        return isFixedHoliday(date) || isVariableHoliday(date);
    }

    public boolean isFixedHoliday(LocalDate date) {

        return fixedRepository.findAll()
                .stream()
                .anyMatch(h -> h.matches(date));
    }

    public boolean isVariableHoliday(LocalDate date) {
        return variableRepository.existsByDate(date);
    }

    public boolean isWorkingDay(LocalDate date) {
        return !isWeekend(date) && !isHoliday(date);
    }

    public boolean isBusinessDay(LocalDate date) {
        return date.getDayOfWeek() != DayOfWeek.SUNDAY
                && !isHoliday(date);
    }

    private boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY
                || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    public boolean isSolidarityDay(LocalDate date, LocalDate solidarityDay) {
        return solidarityDay != null && date.equals(solidarityDay);
    }
}