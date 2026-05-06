package com.jinji.backend.service;

import com.jinji.backend.model.dto.LeaveRequestCreateRequest;
import com.jinji.backend.model.dto.LeaveRequestDTO;
import com.jinji.backend.model.dto.LeaveRequestReviewDTO;
import com.jinji.backend.model.dto.LeaveRequestSummaryDTO;
import com.jinji.backend.model.entity.*;
import com.jinji.backend.model.enums.LeaveRequestStatus;
import com.jinji.backend.model.enums.PeriodType;
import com.jinji.backend.repository.LeaveRequestRepository;
import com.jinji.backend.repository.LeaveRequestReviewRepository;
import com.jinji.backend.repository.LeaveTypeRepository;
import com.jinji.backend.repository.projection.LeaveRequestSummaryRaw;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveRequestReviewRepository leaveRequestReviewRepository;
    private final UserService userService;
    private final EmployeeService employeeService;

    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository,
                               LeaveTypeRepository leaveTypeRepository, LeaveRequestReviewRepository leaveRequestReviewRepository, UserService userService, EmployeeService employeeService) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.leaveRequestReviewRepository = leaveRequestReviewRepository;
        this.userService = userService;
        this.employeeService = employeeService;
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

//    public List<LeaveRequestDTO> getMyLeaveRequests(String username) {
//
//        Employee employee = employeeService.getCurrentEmployee(username);
//
//        return leaveRequestRepository.findByEmployee_Id(employee.getId()).stream()
//                .map(this::mapToDto)
//                .toList();
//    }
//
//    private LeaveRequestDTO mapToDto(LeaveRequest lr) {
//        LeaveRequestDTO dto = new LeaveRequestDTO();
//
//        dto.setId(lr.getId());
//        dto.setCreatedAt(lr.getCreatedAt());
//        dto.setStartDate(lr.getStartDate());
//        dto.setEndDate(lr.getEndDate());
//        dto.setStartPeriod(lr.getStartPeriod());
//        dto.setEndPeriod(lr.getEndPeriod());
//        dto.setStatus(lr.getStatus());
//        dto.setEmployeeComment(lr.getEmployeeComment());
//        dto.setLeaveTypeLabel(
//                lr.getLeaveType() != null ? lr.getLeaveType().getLabel() : null
//        );
//
//        return dto;
//    }


    public LeaveRequestDTO getLeaveRequestById(Long leaveRequestId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        return mapToDto(leaveRequest);
    }

    public List<LeaveRequestDTO> getMyLeaveRequests(String username) {

        Employee employee = employeeService.getCurrentEmployee(username);

        List<LeaveRequest> leaveRequests =
                leaveRequestRepository.findByEmployee_Id(employee.getId());

        return leaveRequests.stream()
                .map(this::mapToDto)
                .toList();
    }

    private LeaveRequestDTO mapToDto(LeaveRequest lr) {
        LeaveRequestDTO dto = new LeaveRequestDTO();

        dto.setId(lr.getId());
        dto.setCreatedAt(lr.getCreatedAt());
        dto.setStartDate(lr.getStartDate());
        dto.setEndDate(lr.getEndDate());
        dto.setStartPeriod(lr.getStartPeriod());
        dto.setEndPeriod(lr.getEndPeriod());
        dto.setStatus(lr.getStatus());
        dto.setEmployeeComment(lr.getEmployeeComment());

        dto.setLeaveTypeLabel(
                lr.getLeaveType() != null ? lr.getLeaveType().getLabel() : null
        );

        List<LeaveRequestReview> reviews =
                leaveRequestReviewRepository.findByLeaveRequest_Id(lr.getId());

        dto.setReviews(
                reviews.stream()
                        .map(this::mapReviewToDto)
                        .toList()
        );

        return dto;
    }

    private LeaveRequestReviewDTO mapReviewToDto(LeaveRequestReview r) {

        LeaveRequestReviewDTO dto = new LeaveRequestReviewDTO();

        dto.setId(r.getId());
        dto.setReviewerRole(r.getReviewerRole());
        dto.setDecision(r.getDecision());
        dto.setComment(r.getComment());
        dto.setReviewedAt(r.getReviewedAt());

        if (r.getReviewedBy() != null) {
            dto.setReviewerId(r.getReviewedBy().getId());
            dto.setReviewerFirstName(r.getReviewedBy().getFirstName());
            dto.setReviewerLastName(r.getReviewedBy().getSurname());
        }

        return dto;
    }

    public List<LeaveRequestSummaryDTO> getMyLeaveRequestsSummary(String username) {

        Employee employee = employeeService.getCurrentEmployee(username);

        return leaveRequestRepository.findLeaveRequestSummaryByEmployee_Id(employee.getId())
                .stream()
                .map(this::mapToSummaryDto)
                .toList();
    }

    private LeaveRequestSummaryDTO mapToSummaryDto(LeaveRequestSummaryRaw r) {

        LeaveRequestSummaryDTO dto = new LeaveRequestSummaryDTO();

        dto.setId(r.getId());
        dto.setLeaveTypeLabel(r.getLeaveTypeLabel());
        dto.setStartDate(r.getStartDate());
        dto.setEndDate(r.getEndDate());
        dto.setStatus(r.getStatus());
        dto.setCreatedAt(r.getCreatedAt());

        return dto;
    }

}