package com.jinji.backend.repository;

import com.jinji.backend.model.entity.LeaveRequest;
import com.jinji.backend.repository.projection.LeaveRequestSummaryRaw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByEmployee_Id(Long employeeId);

    @Query("""
    SELECT lr.id as id,
           lt.label as leaveTypeLabel,
           lr.startDate as startDate,
           lr.endDate as endDate,
           lr.status as status,
           lr.createdAt as createdAt
    FROM LeaveRequest lr
    JOIN lr.leaveType lt
    WHERE lr.employee.id = :employeeId
""")
    List<LeaveRequestSummaryRaw> findLeaveRequestSummaryByEmployee_Id(Long employeeId);
}
