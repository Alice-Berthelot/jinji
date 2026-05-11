package com.jinji.backend.repository;

import com.jinji.backend.model.entity.HrPolicy;
import com.jinji.backend.model.enums.LeaveValidationProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HrPolicyRepository extends JpaRepository<HrPolicy, Long> {

    @Query("""
        SELECT h
        FROM HrPolicy h
    """)
    HrPolicy findHrPolicy();

    @Query("""
        SELECT h.leaveValidation
        FROM HrPolicy h
    """)
    LeaveValidationProcess findLeaveValidation();
}