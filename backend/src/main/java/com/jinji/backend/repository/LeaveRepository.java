package com.jinji.backend.repository;

import com.jinji.backend.model.entity.Employee;
import com.jinji.backend.model.entity.Leave;
import com.jinji.backend.model.entity.LeaveBalance;
import com.jinji.backend.model.projection.LeaveCalendarProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
    @Query("""
    SELECT 
        l.id AS leaveId,
        l.startDate AS startDate,
        l.endDate AS endDate,
        l.status AS status,
        lt.code AS leaveTypeCode,
        lt.label AS leaveTypeLabel
    FROM Leave l
    JOIN l.leaveType lt
    WHERE l.employee.id = :employeeId
""")
    List<LeaveCalendarProjection> findEmployeeCalendar(Long employeeId);

    @Query("""
    SELECT DISTINCT
        e.id AS employeeId,
        e.firstName AS firstName,
        e.surname AS surname,
        lt.code AS leaveTypeCode,
        lt.label AS leaveTypeLabel,
        l.startDate AS startDate,
        l.endDate AS endDate,
        l.status AS status,
        l.id AS leaveId
    FROM Team t
    JOIN t.employees e
    JOIN Leave l ON l.employee = e
    JOIN l.leaveType lt
    WHERE t.manager.id = :managerId
""")
    List<LeaveCalendarProjection> findManagerCalendar(Long managerId);

    @Query("""
    SELECT 
        e.id AS employeeId,
        e.firstName AS firstName,
        e.surname AS surname,
        lt.code AS leaveTypeCode,
        lt.label AS leaveTypeLabel,
        l.startDate AS startDate,
        l.endDate AS endDate,
        l.status AS status,
        l.id AS leaveId
    FROM Leave l
    JOIN l.employee e
    JOIN l.leaveType lt
""")
    List<LeaveCalendarProjection> findHrCalendar();
}

