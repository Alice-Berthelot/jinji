package com.jinji.backend.service.crud;

import com.jinji.backend.model.entity.Leave;
import com.jinji.backend.model.entity.LeaveRequest;
import com.jinji.backend.repository.LeaveRepository;
import com.jinji.backend.repository.projection.LeaveRaw;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class LeaveService {

    private final LeaveRepository leaveRepository;
    private final LeaveBalanceService leaveBalanceService;

    public LeaveService(
            LeaveRepository leaveRepository, LeaveBalanceService leaveBalanceService
    ) {
        this.leaveRepository = leaveRepository;
        this.leaveBalanceService = leaveBalanceService;
    }

//    @Transactional
//    public LeaveDTO createLeave(LeaveCreateRequest request) {
//
//        Employee employee = employeeRepository.findById(request.getEmployeeId())
//                .orElseThrow(() -> new RuntimeException("Employee not found"));
//
//        Leave leave = buildLeave(
//                employee,
//                request.getStartDate(),
//                request.getEndDate(),
//                request.getComment()
//        );
//
//        leaveRepository.save(leave);
//
//        return mapToDto(leave);
//    }

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