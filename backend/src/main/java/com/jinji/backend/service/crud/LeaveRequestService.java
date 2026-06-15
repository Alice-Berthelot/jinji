package com.jinji.backend.service.crud;

import com.jinji.backend.exception.BadRequestException;
import com.jinji.backend.exception.ForbiddenException;
import com.jinji.backend.exception.ResourceNotFoundException;
import com.jinji.backend.mapper.LeaveRequestMapper;
import com.jinji.backend.model.dto.LeaveRequestReviewDTO;
import com.jinji.backend.model.dto.request.LeaveRequestCreateRequest;
import com.jinji.backend.model.dto.request.LeaveRequestCreateReview;
import com.jinji.backend.model.dto.response.LeaveRequestActionResponseDTO;
import com.jinji.backend.model.dto.response.LeaveRequestDTO;
import com.jinji.backend.model.dto.response.LeaveRequestSummaryDTO;
import com.jinji.backend.model.dto.response.MyLeaveRequestSummaryDTO;
import com.jinji.backend.model.entity.*;
import com.jinji.backend.model.enums.*;
import com.jinji.backend.model.projection.LeaveRequestSummary;
import com.jinji.backend.model.projection.MyLeaveRequestSummary;
import com.jinji.backend.repository.LeaveRequestRepository;
import com.jinji.backend.repository.LeaveRequestReviewRepository;
import com.jinji.backend.repository.LeaveTypeRepository;
import com.jinji.backend.service.business.LeaveCalculationService;
import com.jinji.backend.service.business.PermissionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveRequestReviewRepository leaveRequestReviewRepository;
    private final UserService userService;
    private final EmployeeService employeeService;
    private final HrPolicyService hrPolicyService;
    private final LeaveCalculationService leaveCalculationService;
    private final LeaveRequestReviewService leaveRequestReviewService;
    private final LeaveService leaveService;
    private final TeamService teamService;
    private final NotificationService notificationService;
    private final PermissionService permissionService;
    private final LeaveRequestMapper leaveRequestMapper;

    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository,
                               LeaveTypeRepository leaveTypeRepository, LeaveRequestReviewRepository leaveRequestReviewRepository, UserService userService, EmployeeService employeeService, HrPolicyService hrPolicyService, LeaveCalculationService leaveCalculationService, LeaveRequestReviewService leaveRequestReviewService, LeaveService leaveService, TeamService teamService, NotificationService notificationService, PermissionService permissionService, LeaveRequestMapper leaveRequestMapper) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.leaveRequestReviewRepository = leaveRequestReviewRepository;
        this.userService = userService;
        this.employeeService = employeeService;
        this.hrPolicyService = hrPolicyService;
        this.leaveCalculationService = leaveCalculationService;
        this.leaveRequestReviewService = leaveRequestReviewService;
        this.leaveService = leaveService;
        this.teamService = teamService;
        this.notificationService = notificationService;
        this.permissionService = permissionService;
        this.leaveRequestMapper = leaveRequestMapper;
    }

    public LeaveRequestDTO createLeaveRequest(LeaveRequestCreateRequest request) {
        // Ensure the current user has an associated employee, since a leave request must be linked to an employee
        Employee employee = employeeService.getCurrentEmployee();
        if (employee == null) {
            throw new ResourceNotFoundException("No employee linked to user");
        }

        // Ensure that the employee has an INTERNAL status
        if (employee.getStatus() != EmployeeStatus.INTERNAL) {
            throw new ForbiddenException("Only internal employees can create leave requests");
        }

        // Ensure that the leave type exists in the database
        LeaveType leaveType = leaveTypeRepository
                .findByCode(request.getLeaveTypeCode())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Leave type not found with code: " + request.getLeaveTypeCode()
                ));

        PeriodType startPeriod = request.getStartPeriod() != null
                ? request.getStartPeriod()
                : PeriodType.AM;

        PeriodType endPeriod = request.getEndPeriod() != null
                ? request.getEndPeriod()
                : PeriodType.PM;

        BigDecimal numberOfDays = leaveCalculationService.calculateLeaveDays(
                request.getStartDate(),
                request.getEndDate(),
                startPeriod,
                endPeriod
        );

        // Create and persist a new leave request with the appropriate fields
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
        leave.setNumberOfDays(numberOfDays);
        leave.setCreatedAt(LocalDateTime.now());
        leave.setStatus(LeaveRequestStatus.PENDING);
        LeaveRequest saved = leaveRequestRepository.save(leave);

        // Notify the employee and their manager(s)
        User currentUser = userService.getCurrentUser();
        notificationService.create(currentUser, "Votre demande de congés n°"
                + leave.getId() +
                " a bien été créée.");
        List<Employee> managers =
                teamService.findManagersByEmployeeId(employee.getId());
        List<User> managerUsers = managers.stream()
                .map(m -> userService.findByEmployeeId(m.getId()))
                .flatMap(Optional::stream)
                .toList();

        for (User managerUser : managerUsers) {
            notificationService.create(managerUser, "Nouvelle demande de congé de " + employee.getFullName());
        }

        return leaveRequestMapper.toDto(saved);
    }

    public LeaveRequestDTO getLeaveRequestById(Long leaveRequestId) throws RuntimeException {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with id " + leaveRequestId));

        User currentUser = userService.getCurrentUser();
        Employee employeeAuth = currentUser.getEmployee();

        boolean isOwner = employeeAuth.getId().equals(leaveRequest.getEmployee().getId());
        boolean isHr = currentUser.isHr();
        boolean isTeamManager = currentUser.isManager()
                && permissionService.isManagerOf(employeeAuth, leaveRequest.getEmployee());

        if (!isOwner && !isHr && !isTeamManager) {
            throw new ForbiddenException("User is not authorized to read the leave request");
        }

        return mapToDto(leaveRequest);
    }

    public Page<LeaveRequestSummaryDTO> getLeaveRequestsSummary(Pageable pageable) {
        User currentUser = userService.getCurrentUser();
        Employee employeeAuth = currentUser.getEmployee();

        boolean isHr = currentUser.isHr();

        boolean isManager = currentUser.isManager();

        Page<LeaveRequestSummary> leaveRequests;

        if (isHr) {
            leaveRequests = leaveRequestRepository.findAllLeaveRequestsSummary(pageable);
        }
        else if (isManager) {
            leaveRequests = leaveRequestRepository
                    .findLeaveRequestSummaryByManagerId(employeeAuth.getId(), pageable);
        }
        else {
            throw new ForbiddenException(
                    "User is not authorized to read leave requests"
            );
        }

        return leaveRequests.map(r -> {

            LeaveRequestWorkflowStatus workflowStatus =
                    buildWorkflowStatus(
                            r.getStatus(),
                            r.getHasManagerReview()
                    );

            String statusLabel =
                    buildLeaveRequestStatusLabel(workflowStatus);

            return leaveRequestMapper.toSummaryDto(
                    r,
                    workflowStatus,
                    statusLabel
            );
        });
    }

    public Page<MyLeaveRequestSummaryDTO> getMyLeaveRequestsSummary(Pageable pageable) {

        Employee employee = employeeService.getCurrentEmployee();

        Page<MyLeaveRequestSummary> page =
                leaveRequestRepository.findLeaveRequestSummaryByEmployee_Id(
                        employee.getId(),
                        pageable
                );

        return page.map(leaveRequestMapper::toMySummaryDto);
    }

    @Transactional
    public LeaveRequestDTO processLeaveRequest(Long leaveRequestId, LeaveRequestCreateReview reviewRaw) throws RuntimeException {
        // If no leave request matches the provided ID, the request cannot be processed
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with id " + leaveRequestId));

        // If the leave request is already APPROVED or CANCELLED, it cannot be processed further
        LeaveRequestStatus leaveRequestStatus = leaveRequest.getStatus();
        if (leaveRequestStatus == LeaveRequestStatus.APPROVED ||
                leaveRequestStatus == LeaveRequestStatus.REJECTED ||
                leaveRequestStatus == LeaveRequestStatus.CANCELLED) {
            throw new BadRequestException("Leave request with id " + leaveRequestId
                    + " has already been reviewed, with status " + leaveRequestStatus);
        }

        User currentUser = userService.getCurrentUser();
        Employee reviewer = currentUser.getEmployee();

        // An employee cannot process their own leave requests
        boolean isOwner = reviewer.getId().equals(leaveRequest.getEmployee().getId());
        if (isOwner) {
            throw new RuntimeException("User is not authorized to validate their own leave request");
        }

        // Check whether reviews already exist
        List<LeaveRequestReview> reviews = leaveRequestReviewRepository.findByLeaveRequest_Id(leaveRequestId);
        boolean hasManagerReview = reviews.stream()
                .anyMatch(r ->
                        r.getReviewerRole() == LeaveRequestReviewerRole.MANAGER
                );
        boolean hasHrReview = reviews.stream()
                .anyMatch(r ->
                        r.getReviewerRole() == LeaveRequestReviewerRole.HR
                );
        if (hasManagerReview && hasHrReview) {
            throw new ForbiddenException("Leave request with id " + leaveRequest.getId()
                    + " has already been reviewed by Manager AND HR");
        }

        boolean isHr = currentUser.isHr();
        boolean isTeamManager =
                currentUser.isManager()
                        && permissionService.isManagerOf(reviewer, leaveRequest.getEmployee());

        // Determine which validation process to apply
        LeaveValidationProcess process = hrPolicyService.getLeaveValidation();

        switch (process) {
            case MANAGER_THEN_HR -> {
                    if (!isHr && !isTeamManager) {
                        throw new ForbiddenException(
                                "User is not authorized to review leave request with id " + leaveRequest.getId()
                        );
                    }
                    processManagerThenHr(
                            leaveRequest,
                            reviewRaw,
                            reviewer,
                            isTeamManager,
                            isHr,
                            hasManagerReview
                    );
            }
            case MANAGER_ONLY -> {
                    if (!isTeamManager) {
                        throw new ForbiddenException(
                                "User is not authorized to review leave request with id " + leaveRequest.getId()
                        );
                    }

                    processManagerOnly(
                            leaveRequest,
                            reviewRaw,
                            reviewer
                    );
            }

            default ->
                    throw new IllegalStateException(
                            "Unsupported validation process: " + process
                    );
        }

        return mapToDto(leaveRequest);
    }

    private void processManagerOnly(
            LeaveRequest leaveRequest,
            LeaveRequestCreateReview reviewRaw,
            Employee reviewer
    ) {
        LeaveRequestDecision decision = reviewRaw.getDecision();

//  1. Create leave review
        leaveRequestReviewService.createLeaveRequestReview(
                leaveRequest,
                reviewer,
                LeaveRequestReviewerRole.MANAGER,
                reviewRaw
        );

        Optional<User> employeeUser =
                userService.findByEmployeeId(leaveRequest.getEmployee().getId());

// 2. Update leave request status
        switch (decision) {
            case APPROVED -> {
                leaveRequest.setStatus(LeaveRequestStatus.APPROVED);
                // 3. Create leave
                leaveService.createLeaveFromRequest(leaveRequest);
            }
            case REJECTED -> {
                leaveRequest.setStatus(LeaveRequestStatus.REJECTED);
            }
        }

        leaveRequestRepository.save(leaveRequest);

        // 4. (or 3. if REJECTED) send notifications
        employeeUser.ifPresent(user ->
                notificationService.notifyFinalDecisionToEmployee(
                        leaveRequest.getId(),
                        user,
                        decision
                )
        );

        List<User> hrUsers = userService.findHrUsers();
        for (User hrUser : hrUsers) {
            notificationService.notifyManagerFinalDecisionToHr(
                    leaveRequest.getId(),
                    hrUser,
                    decision,
                    leaveRequest.getEmployee().getFullName()
            );
        }
    }

    private void processManagerThenHr(
            LeaveRequest leaveRequest,
            LeaveRequestCreateReview reviewRaw,
            Employee reviewer,
            boolean isManager,
            boolean isHr,
            boolean hasManagerReview
    ) {

        LeaveRequestDecision decision = reviewRaw.getDecision();

        Optional<User> employeeUser =
                userService.findByEmployeeId(leaveRequest.getEmployee().getId());

        // MANAGER REVIEW
        if (!hasManagerReview) {
            if (!isManager) {
                throw new ForbiddenException(
                        "Manager review is required first"
                );
            }

            //  1. Create leave review
            leaveRequestReviewService.createLeaveRequestReview(
                    leaveRequest,
                    reviewer,
                    LeaveRequestReviewerRole.MANAGER,
                    reviewRaw
            );

            leaveRequestRepository.save(leaveRequest);

            // 2. send notifications to employee and HR
            employeeUser.ifPresent(user ->
                    notificationService.notifyManagerDecisionToEmployee(
                            leaveRequest.getId(),
                            user,
                            decision
                    )
            );

            List<User> hrUsers = userService.findHrUsers();
            for (User hrUser : hrUsers) {
                notificationService.notifyManagerDecisionToHr(
                        leaveRequest.getId(),
                        hrUser,
                        decision,
                        leaveRequest.getEmployee().getFullName()
                );
            }

            return;
        }

        // HR REVIEW
        if (!isHr) {
            throw new ForbiddenException(
                    "Reviewer must be HR"
            );
        }

        //  1. Create leave review
        leaveRequestReviewService.createLeaveRequestReview(
                leaveRequest,
                reviewer,
                LeaveRequestReviewerRole.HR,
                reviewRaw
        );

        // 2. Update leave request status
        switch (decision) {
            case APPROVED -> {
                leaveRequest.setStatus(LeaveRequestStatus.APPROVED);
                leaveRequestRepository.save(leaveRequest);
                // 3. Create leave
                leaveService.createLeaveFromRequest(leaveRequest);
            }
            case REJECTED -> {
                leaveRequest.setStatus(LeaveRequestStatus.REJECTED);
                leaveRequestRepository.save(leaveRequest);
            }
        }

        // 4. (or 3. if REJECTED) Send notifications to employee and manager(s)
        employeeUser.ifPresent(user ->
                notificationService.notifyFinalDecisionToEmployee(
                        leaveRequest.getId(),
                        user,
                        decision
                )
        );

        List<Employee> managers =
                teamService.findManagersByEmployeeId(leaveRequest.getEmployee().getId());
        List<User> managerUsers = managers.stream()
                .map(m -> userService.findByEmployeeId(m.getId()))
                .flatMap(Optional::stream)
                .toList();

        for (User managerUser : managerUsers) {
            notificationService.notifyHrFinalDecisionToManager(leaveRequest.getId(), managerUser, decision,leaveRequest.getEmployee().getFullName());
        }
    }



    @Transactional
    public LeaveRequestActionResponseDTO cancelLeaveRequest(Long leaveRequestId) {
        User currentUser = userService.getCurrentUser();
        Employee currentEmployee = currentUser.getEmployee();

        if (currentEmployee == null) {
            throw new ResourceNotFoundException("No employee linked to user");
        }

        LeaveRequest leaveRequest =
                leaveRequestRepository.findByIdAndEmployee_Id(
                                leaveRequestId,
                                currentEmployee.getId()
                        )
                        .orElseThrow(() -> new ForbiddenException(
                                "User is not authorized to cancel this leave request"
                        ));

        if (leaveRequest.getStatus() == LeaveRequestStatus.CANCELLED) {
            throw new BadRequestException(
                    "Leave request is already cancelled"
            );
        }

        if (leaveRequest.getStatus() == LeaveRequestStatus.APPROVED) {
            throw new BadRequestException(
                    "Approved leave request cannot be cancelled"
            );
        }

        leaveRequest.setStatus(LeaveRequestStatus.CANCELLED);

        leaveRequestRepository.save(leaveRequest);

        notificationService.create(currentUser, "Votre demande d'absence n°" + leaveRequest.getId() + " a été annulée.");

        return leaveRequestMapper.toActionResponseDto(leaveRequest);
    }

    // HELPERS
    private LeaveRequestDTO mapToDto(LeaveRequest lr) {
        LeaveRequestDTO dto = new LeaveRequestDTO();

        dto.setLeaveRequestId(lr.getId());
        dto.setEmployeeId(lr.getEmployee().getId());
        dto.setEmployeeFirstName(lr.getEmployee().getFirstName());
        dto.setEmployeeSurname(lr.getEmployee().getSurname());
        dto.setCreatedAt(lr.getCreatedAt());
        dto.setStartDate(lr.getStartDate());
        dto.setEndDate(lr.getEndDate());
        dto.setStartPeriod(lr.getStartPeriod());
        dto.setEndPeriod(lr.getEndPeriod());
        dto.setStatus(lr.getStatus());
        dto.setEmployeeComment(lr.getEmployeeComment());
        dto.setNumberOfDays(lr.getNumberOfDays());

        dto.setLeaveTypeLabel(
                lr.getLeaveType() != null ? lr.getLeaveType().getLabel() : null
        );

        List<LeaveRequestReview> reviews =
                leaveRequestReviewRepository.findByLeaveRequest_Id(lr.getId());

        boolean hasManagerReview = reviews.stream()
                .anyMatch(r ->
                        r.getReviewerRole() == LeaveRequestReviewerRole.MANAGER
                );

        boolean hasHrReview = reviews.stream()
                .anyMatch(r ->
                        r.getReviewerRole() == LeaveRequestReviewerRole.HR
                );

        LeaveRequestWorkflowStatus workflowStatus =
                buildWorkflowStatus(
                        lr.getStatus(),
                        hasManagerReview
                );

        dto.setWorkflowStatus(workflowStatus);

        dto.setStatusLabel(
                buildLeaveRequestStatusLabel(workflowStatus)
        );

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

    private LeaveRequestWorkflowStatus buildWorkflowStatus(
            LeaveRequestStatus status,
            Boolean hasManagerReview
    ) {

        if (status == LeaveRequestStatus.APPROVED) {
            return LeaveRequestWorkflowStatus.APPROVED;
        }

        if (status == LeaveRequestStatus.REJECTED) {
            return LeaveRequestWorkflowStatus.REJECTED;
        }

        if (status == LeaveRequestStatus.CANCELLED) {
            return LeaveRequestWorkflowStatus.CANCELLED;
        }

        LeaveValidationProcess validationProcess =
                hrPolicyService.getLeaveValidation();

        if (Boolean.FALSE.equals(hasManagerReview)) {
            return LeaveRequestWorkflowStatus.PENDING_MANAGER;
        }

        if (
                validationProcess == LeaveValidationProcess.MANAGER_THEN_HR
                        && Boolean.TRUE.equals(hasManagerReview)
        ) {
            return LeaveRequestWorkflowStatus.PENDING_HR;
        }

        return LeaveRequestWorkflowStatus.PENDING;
    }

    private String buildLeaveRequestStatusLabel(
            LeaveRequestWorkflowStatus workflowStatus
    ) {

        return switch (workflowStatus) {
            case APPROVED -> "Validée";
            case REJECTED -> "Refusée";
            case CANCELLED -> "Annulée";
            case PENDING_MANAGER -> "En attente de validation Manager";
            case PENDING_HR -> "En attente de validation RH";
            case PENDING -> "En attente";
        };
    }
}