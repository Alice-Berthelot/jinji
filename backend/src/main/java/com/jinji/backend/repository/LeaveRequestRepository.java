package com.jinji.backend.repository;

import com.jinji.backend.model.entity.LeaveRequest;
import com.jinji.backend.repository.projection.LeaveRequestSummaryRaw;
import com.jinji.backend.repository.projection.MyLeaveRequestSummaryRaw;
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
           lr.createdAt as createdAt,
           lr.numberOfDays as numberOfDays
    FROM LeaveRequest lr
    JOIN lr.leaveType lt
    WHERE lr.employee.id = :employeeId
""")
    List<MyLeaveRequestSummaryRaw> findLeaveRequestSummaryByEmployee_Id(Long employeeId);


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
    List<LeaveRequestSummaryRaw> findAllLeaveRequestsSummary();

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
    List<LeaveRequestSummaryRaw> findLeaveRequestSummaryByManagerId(Long managerId);
}
