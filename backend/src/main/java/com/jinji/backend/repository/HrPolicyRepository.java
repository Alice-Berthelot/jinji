package com.jinji.backend.repository;

import com.jinji.backend.model.entity.HrPolicy;
import com.jinji.backend.model.enums.AnnualLeaveAccrualPeriod;
import com.jinji.backend.model.enums.AnnualLeaveDayType;
import com.jinji.backend.model.enums.LeaveValidationProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface HrPolicyRepository extends JpaRepository<HrPolicy, Long> {

    Optional<HrPolicy> findTopByOrderByIdAsc();

    @Query("""
        SELECT h.allowAnnualLeaveCarryover
        FROM HrPolicy h
    """)
    Boolean findAllowAnnualLeaveCarryover();

}