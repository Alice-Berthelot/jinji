package com.jinji.backend.service.crud;

import com.jinji.backend.model.entity.HrPolicy;
import com.jinji.backend.model.entity.PublicHolidayVariable;
import com.jinji.backend.model.enums.AnnualLeaveDayType;
import com.jinji.backend.model.enums.LeaveValidationProcess;
import com.jinji.backend.repository.HrPolicyRepository;
import com.jinji.backend.repository.PublicHolidayVariableRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class HrPolicyService {
    private final HrPolicyRepository repository;
    private final PublicHolidayVariableRepository publicHolidayVariableRepository;

    public HrPolicyService(HrPolicyRepository repository, PublicHolidayVariableRepository publicHolidayVariableRepository) {
        this.repository = repository;
        this.publicHolidayVariableRepository = publicHolidayVariableRepository;
    }

    public HrPolicy getHrPolicy() {
        return repository.findHrPolicy();
    }

    public LeaveValidationProcess getLeaveValidation() {
        return repository.findLeaveValidation();
    }

    public AnnualLeaveDayType getAnnualLeaveDayType() {
        return repository.findAnnualLeaveDayType();
    }

    public LocalDate getSolidarityDay() {
        return getHrPolicy().getSolidarityDay();
    }

    public LocalDate getEffectiveSolidarityDay() {

        HrPolicy policy = getHrPolicy();

        // Applying specific solidarity day configured by HR
        if (policy.getSolidarityDay() != null) {
            return policy.getSolidarityDay();
        }

        // Default = Pentecôte
        int year = LocalDate.now().getYear();

        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);

        return publicHolidayVariableRepository
                .findByLabelAndDateBetween(
                        "Lundi de Pentecôte",
                        start,
                        end
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Pentecost Monday not configured"
                        )
                )
                .getDate();
    }
}
