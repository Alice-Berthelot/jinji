package com.jinji.backend.repository;

import com.jinji.backend.model.entity.LeaveRequest;
import com.jinji.backend.model.projection.MyLeaveRequestSummary;
import com.jinji.backend.model.projection.LeaveRequestSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    @Query("""
    SELECT lr.id as id,
           lt.label as leaveTypeLabel,
           lr.startDate as startDate,
           lr.endDate as endDate,
           lr.status as status,
           lr.createdAt as createdAt,
           lr.numberOfDays as numberOfDays
    FROM LeaveRequest lr
    JOIN lr.leaveType lt
    WHERE lr.employee.id = :employeeId
""")
    Page<MyLeaveRequestSummary> findLeaveRequestSummaryByEmployee_Id(
            @Param("employeeId") Long employeeId,
            Pageable pageable
    );


    @Query("""
    SELECT
        lr.id AS id,
        lt.label AS leaveTypeLabel,
        lr.startDate AS startDate,
        lr.endDate AS endDate,
        lr.status AS status,
        lr.createdAt AS createdAt,
        lr.numberOfDays AS numberOfDays,

        e.firstName AS employeeFirstName,
        e.surname AS employeeSurname,

        CASE WHEN EXISTS (
            SELECT r1.id
            FROM LeaveRequestReview r1
            WHERE r1.leaveRequest.id = lr.id
            AND r1.reviewerRole = 'HR'
        )
        THEN true ELSE false END AS hasHrReview,

        CASE WHEN EXISTS (
            SELECT r2.id
            FROM LeaveRequestReview r2
            WHERE r2.leaveRequest.id = lr.id
            AND r2.reviewerRole = 'MANAGER'
        )
        THEN true ELSE false END AS hasManagerReview

    FROM LeaveRequest lr
    JOIN lr.leaveType lt
    JOIN lr.employee e
""")
    Page<LeaveRequestSummary> findAllLeaveRequestsSummary(Pageable pageable);

    @Query("""
    SELECT
        lr.id AS id,
        lt.label AS leaveTypeLabel,
        lr.startDate AS startDate,
        lr.endDate AS endDate,
        lr.status AS status,
        lr.createdAt AS createdAt,
        lr.numberOfDays AS numberOfDays,

        e.firstName AS employeeFirstName,
        e.surname AS employeeSurname,

        CASE WHEN EXISTS (
            SELECT r1.id
            FROM LeaveRequestReview r1
            WHERE r1.leaveRequest.id = lr.id
            AND r1.reviewerRole = 'HR'
        )
        THEN true ELSE false END AS hasHrReview,

        CASE WHEN EXISTS (
            SELECT r2.id
            FROM LeaveRequestReview r2
            WHERE r2.leaveRequest.id = lr.id
            AND r2.reviewerRole = 'MANAGER'
        )
        THEN true ELSE false END AS hasManagerReview

    FROM LeaveRequest lr
    JOIN lr.leaveType lt
    JOIN lr.employee e
    JOIN e.teams t

    WHERE t.manager.id = :managerId
""")
    Page<LeaveRequestSummary> findLeaveRequestSummaryByManagerId(Long managerId, Pageable pageable);

    Optional<LeaveRequest> findByIdAndEmployee_Id(
            Long leaveRequestId,
            Long employeeId
    );
}
