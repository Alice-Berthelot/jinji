package com.jinji.backend.service.crud;

import com.jinji.backend.mapper.LeaveMapper;
import com.jinji.backend.model.dto.LeaveCreateRequest;
import com.jinji.backend.model.dto.LeaveDTO;
import com.jinji.backend.model.entity.*;
import com.jinji.backend.model.enums.PeriodType;
import com.jinji.backend.repository.EmployeeRepository;
import com.jinji.backend.repository.LeaveRepository;
import com.jinji.backend.repository.LeaveTypeRepository;
import com.jinji.backend.repository.projection.LeaveRaw;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class LeaveService {

    private final LeaveRepository leaveRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveBalanceService leaveBalanceService;
    private final UserService userService;
    private final LeaveMapper leaveMapper;

    public LeaveService(
            LeaveRepository leaveRepository, LeaveTypeRepository leaveTypeRepository, EmployeeRepository employeeRepository, LeaveBalanceService leaveBalanceService, UserService userService, LeaveMapper leaveMapper
    ) {
        this.leaveRepository = leaveRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.employeeRepository = employeeRepository;
        this.leaveBalanceService = leaveBalanceService;
        this.userService = userService;
        this.leaveMapper = leaveMapper;
    }

    @Transactional
    public LeaveDTO createLeave(LeaveCreateRequest request) {
        User currentUser = userService.getCurrentUser();
        boolean isHr = currentUser.isHr();
        Employee employeeAuth = currentUser.getEmployee();
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if ((employeeAuth.getId().equals(employee.getId())) && !isHr) {
            throw new RuntimeException("User is not authorized to create a leave for employee " + employee.getFullName());
        }


        LeaveType leaveType = leaveTypeRepository
                .findByCode(request.getLeaveTypeCode())
                .orElseThrow(() -> new RuntimeException(
                        "Leave type not found with code: " + request.getLeaveTypeCode()
                ));

        LeaveRaw leaveRaw = new LeaveRaw();

        leaveRaw.setEmployee(employee);
        leaveRaw.setStartDate(request.getStartDate());
        leaveRaw.setEndDate(request.getEndDate());
        PeriodType startPeriod = request.getStartPeriod() != null
                ? request.getStartPeriod()
                : PeriodType.AM;
        leaveRaw.setStartPeriod(startPeriod);
        PeriodType endPeriod = request.getEndPeriod() != null
                ? request.getEndPeriod()
                : PeriodType.PM;
        leaveRaw.setEndPeriod(endPeriod);
        leaveRaw.setCreatedBy(employeeAuth);
        leaveRaw.setCreatedAt(request.getCreatedAt());
        leaveRaw.setLeaveType(leaveType);

        if (leaveRaw.getLeaveType().isBalanceManaged()) {
// TODO: add numberOfDays
//         leaveRaw.setNumberOfDays(request.getNumberOfDays());
            System.out.println("numberOfDays to be implemented");
            leaveRaw.setNumberOfDays(BigDecimal.valueOf(1));
        }


        Leave leave = buildLeave(leaveRaw);

        return leaveMapper.toDto(leave);
    }

    @Transactional
    public void createLeaveFromRequest(LeaveRequest leaveRequest) {

        LeaveRaw leaveRaw = new LeaveRaw();

        leaveRaw.setStartDate(leaveRequest.getStartDate());
        leaveRaw.setEndDate(leaveRequest.getEndDate());
        leaveRaw.setStartPeriod(leaveRequest.getStartPeriod());
        leaveRaw.setEndPeriod(leaveRequest.getEndPeriod());
        leaveRaw.setCreatedBy(leaveRequest.getEmployee());
        leaveRaw.setEmployee(leaveRequest.getEmployee());
        leaveRaw.setLeaveRequest(leaveRequest);
        leaveRaw.setLeaveType(leaveRequest.getLeaveType());
        leaveRaw.setNumberOfDays(leaveRequest.getNumberOfDays());

        Leave leave = buildLeave(leaveRaw);
    }

    @Transactional
    private Leave buildLeave(LeaveRaw leaveRaw) {

        Leave leave = new Leave();

        leave.setStartDate(leaveRaw.getStartDate());
        leave.setEndDate(leaveRaw.getEndDate());
        leave.setStartPeriod(leaveRaw.getStartPeriod());
        leave.setEndPeriod(leaveRaw.getEndPeriod());
        leave.setCreatedAt(leaveRaw.getCreatedAt() == null ? LocalDateTime.now() : leaveRaw.getCreatedAt());
        leave.setCreatedBy(leaveRaw.getCreatedBy());
        leave.setEmployee(leaveRaw.getEmployee());
        leave.setLeaveRequest(leaveRaw.getLeaveRequest());
        leave.setLeaveType(leaveRaw.getLeaveType());

        leaveRepository.save(leave);

        if (leave.getLeaveType().isBalanceManaged()) {
            leaveBalanceService.deductLeaveBalance(leave.getEmployee(), leave.getLeaveType(), leaveRaw.getNumberOfDays());
        }

        return leave;
    }
}