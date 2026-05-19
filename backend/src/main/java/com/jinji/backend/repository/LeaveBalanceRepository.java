package com.jinji.backend.repository;

import com.jinji.backend.model.entity.Employee;
import com.jinji.backend.model.entity.LeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {

    List<LeaveBalance> findByEmployee_Id(Long employeeId);

    boolean existsByEmployeeAndAcquisitionStartDate(
            Employee employee,
            LocalDate acquisitionStartDate
    );
}