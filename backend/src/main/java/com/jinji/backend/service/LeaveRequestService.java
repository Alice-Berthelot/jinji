package com.jinji.backend.service;

import com.jinji.backend.model.dto.LeaveRequestCreateRequest;
import com.jinji.backend.model.entity.Employee;
import com.jinji.backend.model.entity.LeaveRequest;
import com.jinji.backend.model.entity.LeaveType;
import com.jinji.backend.model.entity.User;
import com.jinji.backend.model.enums.LeaveRequestStatus;
import com.jinji.backend.model.enums.PeriodType;
import com.jinji.backend.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final UserService userService;

    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository,
                               LeaveTypeRepository leaveTypeRepository, UserService userService) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.userService = userService;
    }

    public String createLeaveRequest(LeaveRequestCreateRequest request) {
        User currentUser = userService.getCurrentUser();

        Employee employee = currentUser.getEmployee();

        if (employee == null) {
            throw new RuntimeException("No employee linked to user");
        }

        LeaveType leaveType = leaveTypeRepository
                .findByCode(request.getLeaveTypeCode())
                .orElseThrow(() -> new RuntimeException(
                        "Leave type not found with code: " + request.getLeaveTypeCode()
                ));

        LeaveRequest leave = new LeaveRequest();
        leave.setEmployee(employee);
        leave.setLeaveType(leaveType);
        leave.setStartDate(request.getStartDate());
        leave.setEndDate(request.getEndDate());
        leave.setStartPeriod(
                request.getStartPeriod() != null ? request.getStartPeriod() : PeriodType.AM
        );
        leave.setEndPeriod(
                request.getEndPeriod() != null ? request.getEndPeriod() : PeriodType.PM
        );
        leave.setEmployeeComment(request.getEmployeeComment());
        leave.setCreatedAt(LocalDateTime.now());
        leave.setStatus(LeaveRequestStatus.PENDING);
        leaveRequestRepository.save(leave);

        return "Leave request submitted successfully";
    }
}