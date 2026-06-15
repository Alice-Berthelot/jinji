package com.jinji.backend.service.crud;

import com.jinji.backend.exception.ResourceNotFoundException;
import com.jinji.backend.mapper.HrPolicyMapper;
import com.jinji.backend.model.dto.response.HrPolicyDTO;
import com.jinji.backend.model.entity.HrPolicy;
import com.jinji.backend.model.enums.AnnualLeaveAccrualPeriod;
import com.jinji.backend.model.enums.AnnualLeaveDayType;
import com.jinji.backend.model.enums.LeaveValidationProcess;
import com.jinji.backend.repository.HrPolicyRepository;
import com.jinji.backend.repository.PublicHolidayVariableRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class HrPolicyService {
    private final HrPolicyRepository hrPolicyRepository;
    private final PublicHolidayVariableRepository publicHolidayVariableRepository;
    private final HrPolicyMapper hrPolicyMapper;

    public HrPolicyService(HrPolicyRepository hrPolicyRepository, PublicHolidayVariableRepository publicHolidayVariableRepository, HrPolicyMapper hrPolicyMapper) {
        this.hrPolicyRepository = hrPolicyRepository;
        this.publicHolidayVariableRepository = publicHolidayVariableRepository;
        this.hrPolicyMapper = hrPolicyMapper;
    }

    public HrPolicyDTO getHrPolicyDto() {
        return hrPolicyMapper.toDto(
                hrPolicyRepository.findTopByOrderByIdAsc()
                        .orElseThrow()
        );
    }

    public HrPolicy getHrPolicy() {
        return hrPolicyRepository.findTopByOrderByIdAsc()
                .orElseThrow(() -> new IllegalStateException("HR Policy not found"));
    }

    public LeaveValidationProcess getLeaveValidation() {
        return getHrPolicy().getLeaveValidation();
    }

    public AnnualLeaveDayType getAnnualLeaveDayType() {
        return getHrPolicy().getAnnualLeaveDayType();
    }

    public LocalDate getEffectiveSolidarityDay() {

        HrPolicy hrPolicy = getHrPolicy();

        // Applying specific solidarity day configured by HR
        if (hrPolicy.getSolidarityDay() != null) {
            return hrPolicy.getSolidarityDay();
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
                        new ResourceNotFoundException(
                                "Pentecost Monday not configured for year " + year
                        )
                )
                .getDate();
    }

    public AnnualLeaveAccrualPeriod getAnnualLeaveAccrualPeriod() {
        return getHrPolicy().getAnnualLeaveAccrualPeriod();
    }

    public HrPolicyDTO updateLeaveValidation(LeaveValidationProcess newValue) {
        HrPolicy policy = getHrPolicy();

        policy.setLeaveValidation(newValue);

        HrPolicy saved = hrPolicyRepository.save(policy);

        return hrPolicyMapper.toDto(saved);
    }
}
