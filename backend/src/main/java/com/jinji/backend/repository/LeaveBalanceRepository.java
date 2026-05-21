package com.jinji.backend.repository;

import com.jinji.backend.model.entity.Employee;
import com.jinji.backend.model.entity.LeaveBalance;
import com.jinji.backend.model.entity.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {

    List<LeaveBalance> findByEmployee_Id(Long employeeId);

    boolean existsByEmployeeAndAcquisitionStartDate(
            Employee employee,
            LocalDate acquisitionStartDate
    );

    List<LeaveBalance>
    findByEmployeeAndLeaveTypeOrderByAcquisitionStartDateAsc(
            Employee employee,
            LeaveType leaveType
    );

    Optional<LeaveBalance>
    findTopByEmployeeAndLeaveTypeOrderByAcquisitionStartDateDesc(
            Employee employee,
            LeaveType leaveType
    );
}