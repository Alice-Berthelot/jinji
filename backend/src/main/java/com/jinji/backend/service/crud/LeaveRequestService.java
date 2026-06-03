package com.jinji.backend.service.crud;

import com.jinji.backend.exception.BadRequestException;
import com.jinji.backend.exception.ForbiddenException;
import com.jinji.backend.exception.ResourceNotFoundException;
import com.jinji.backend.mapper.LeaveRequestMapper;
import com.jinji.backend.model.dto.LeaveRequestDTO;
import com.jinji.backend.model.dto.LeaveRequestReviewDTO;
import com.jinji.backend.model.dto.LeaveRequestSummaryDTO;
import com.jinji.backend.model.dto.MyLeaveRequestSummaryDTO;
import com.jinji.backend.model.dto.request.LeaveRequestCreateRequest;
import com.jinji.backend.model.dto.request.LeaveRequestCreateReview;
import com.jinji.backend.model.dto.response.LeaveRequestActionResponseDTO;
import com.jinji.backend.model.entity.*;
import com.jinji.backend.model.enums.*;
import com.jinji.backend.repository.LeaveRequestRepository;
import com.jinji.backend.repository.LeaveRequestReviewRepository;
import com.jinji.backend.repository.LeaveTypeRepository;
import com.jinji.backend.repository.projection.LeaveRequestSummaryRaw;
import com.jinji.backend.repository.projection.MyLeaveRequestSummaryRaw;
import com.jinji.backend.service.business.LeaveCalculationService;
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
    private final NotificationService notificationService;
    private final LeaveRequestMapper leaveRequestMapper;

    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository,
                               LeaveTypeRepository leaveTypeRepository, LeaveRequestReviewRepository leaveRequestReviewRepository, UserService userService, EmployeeService employeeService, HrPolicyService hrPolicyService, LeaveCalculationService leaveCalculationService, LeaveRequestReviewService leaveRequestReviewService, LeaveService leaveService, NotificationService notificationService, LeaveRequestMapper leaveRequestMapper) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.leaveRequestReviewRepository = leaveRequestReviewRepository;
        this.userService = userService;
        this.employeeService = employeeService;
        this.hrPolicyService = hrPolicyService;
        this.leaveCalculationService = leaveCalculationService;
        this.leaveRequestReviewService = leaveRequestReviewService;
        this.leaveService = leaveService;
        this.notificationService = notificationService;
        this.leaveRequestMapper = leaveRequestMapper;
    }

    public String createLeaveRequest(LeaveRequestCreateRequest request) {
        User currentUser = userService.getCurrentUser();

        Employee employee = currentUser.getEmployee();

        if (employee == null) {
            throw new ResourceNotFoundException("No employee linked to user");
        }

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
        leaveRequestRepository.save(leave);

        return "Leave request submitted successfully";
    }

    public LeaveRequestDTO getLeaveRequestById(Long leaveRequestId) throws RuntimeException {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with id " + leaveRequestId));

        User currentUser = userService.getCurrentUser();
        Employee employeeAuth = currentUser.getEmployee();

        boolean isOwner = employeeAuth.getId().equals(leaveRequest.getEmployee().getId());
        boolean hasHrRole = currentUser.getRoles().stream()
                .anyMatch(role -> role.getLabel().equalsIgnoreCase("HUMAN RESOURCES"));
        boolean hasTeamManagerRole = currentUser.getRoles().stream()
                .anyMatch(role -> role.getLabel().equalsIgnoreCase("MANAGER")) && isManagerOf(employeeAuth, leaveRequest.getEmployee());

        if (!isOwner && !hasHrRole && !hasTeamManagerRole) {
            throw new ForbiddenException("User is not authorized to read the leave request");
        }


        return mapToDto(leaveRequest);
    }

    private boolean isManagerOf(Employee manager, Employee employee) {
        return employee.getTeams().stream()
                .anyMatch(team -> team.getManager() != null
                        && team.getManager().getId().equals(manager.getId()));
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
                        hasManagerReview,
                        hasHrReview
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

    public List<LeaveRequestSummaryDTO> getLeaveRequestsSummary(String username) {
        User currentUser = userService.getCurrentUser();
        Employee employeeAuth = currentUser.getEmployee();

        currentUser.getRoles()
                .forEach(role -> System.out.println(role.getLabel()));

        boolean hasHrRole = currentUser.getRoles().stream()
                .anyMatch(role -> role.getCode() == RoleEnum.HR);

        boolean hasManagerRole = currentUser.getRoles().stream()
                .anyMatch(role -> role.getCode() == RoleEnum.MANAGER);

        List<LeaveRequestSummaryRaw> leaveRequests;

        if (hasHrRole) {
            leaveRequests = leaveRequestRepository.findAllLeaveRequestsSummary();
        }
        else if (hasManagerRole) {
            leaveRequests = leaveRequestRepository
                    .findLeaveRequestSummaryByManagerId(employeeAuth.getId());
        }
        else {
            throw new ForbiddenException(
                    "User is not authorized to read leave requests"
            );
        }

        return leaveRequests.stream()
                .map(this::mapToSummaryDto)
                .toList();
    }

    public List<MyLeaveRequestSummaryDTO> getMyLeaveRequestsSummary(String username) {

        Employee employee = employeeService.getCurrentEmployee(username);

        return leaveRequestRepository.findLeaveRequestSummaryByEmployee_Id(employee.getId())
                .stream()
                .map(this::mapToMySummaryDto)
                .toList();
    }

    private MyLeaveRequestSummaryDTO mapToMySummaryDto(MyLeaveRequestSummaryRaw r) {

        MyLeaveRequestSummaryDTO dto = new MyLeaveRequestSummaryDTO();

        dto.setId(r.getId());
        dto.setLeaveTypeLabel(r.getLeaveTypeLabel());
        dto.setStartDate(r.getStartDate());
        dto.setEndDate(r.getEndDate());
        dto.setStatus(r.getStatus());
        dto.setCreatedAt(r.getCreatedAt());

        return dto;
    }

    private LeaveRequestSummaryDTO mapToSummaryDto(LeaveRequestSummaryRaw r) {

        LeaveRequestSummaryDTO dto = new LeaveRequestSummaryDTO();

        dto.setId(r.getId());
        dto.setLeaveTypeLabel(r.getLeaveTypeLabel());
        dto.setStartDate(r.getStartDate());
        dto.setEndDate(r.getEndDate());
        dto.setStatus(r.getStatus());
        dto.setCreatedAt(r.getCreatedAt());
        dto.setNumberOfDays(r.getNumberOfDays());

        dto.setEmployeeFirstName(r.getEmployeeFirstName());
        dto.setEmployeeSurname(r.getEmployeeSurname());

        dto.setHasHrReview(r.getHasHrReview());
        dto.setHasManagerReview(r.getHasManagerReview());

        LeaveRequestWorkflowStatus workflowStatus =
                buildWorkflowStatus(
                        r.getStatus(),
                        r.getHasManagerReview(),
                        r.getHasHrReview()
                );

        dto.setWorkflowStatus(workflowStatus);

        dto.setStatusLabel(
                buildLeaveRequestStatusLabel(workflowStatus)
        );

        return dto;
    }

    private LeaveRequestWorkflowStatus buildWorkflowStatus(
            LeaveRequestStatus status,
            Boolean hasManagerReview,
            Boolean hasHrReview
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

    @Transactional
    public LeaveRequestDTO processLeaveRequest(Long leaveRequestId, LeaveRequestCreateReview reviewRaw) throws RuntimeException {
        // If no leave request matches the provided ID, the request cannot be processed
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with id " + leaveRequestId));

        // If the leave request is already APPROVED or CANCELLED, it cannot be processed further
        LeaveRequestStatus leaveRequestStatus = leaveRequest.getStatus();
        if (leaveRequestStatus == LeaveRequestStatus.APPROVED || leaveRequestStatus == LeaveRequestStatus.CANCELLED) {
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
                        && isManagerOf(reviewer, leaveRequest.getEmployee());


        // Determine which validation process to apply
        LeaveValidationProcess process = hrPolicyService.getLeaveValidation();

        // MANAGER_THEN_HR
        if (process == LeaveValidationProcess.MANAGER_THEN_HR) {
            processManagerThenHr(
                    leaveRequest,
                    reviewRaw,
                    reviewer,
                    isTeamManager,
                    isHr,
                    hasManagerReview
            );

        } else if (isTeamManager) { // MANAGER_ONLY (DEFAULT)
            processManagerOnly(
                    leaveRequest,
                    reviewRaw,
                    reviewer,
                    isTeamManager
            );
        }


        return mapToDto(leaveRequest);
    }

    private void processManagerOnly(
            LeaveRequest leaveRequest,
            LeaveRequestCreateReview reviewRaw,
            Employee reviewer,
            boolean isManager
    ) {

        if (!isManager) {
            throw new ForbiddenException(
                    "User is not authorized to review this leave request"
            );
        }

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
        // TODO: send notifications (HR)
        employeeUser.ifPresent(user ->
                notifyFinalDecisionToEmployee(
                        leaveRequest.getId(),
                        user,
                        decision
                )
        );
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

            // 2. TODO: send notifications (HR)
            employeeUser.ifPresent(user ->
                    notifyManagerDecisionToEmployee(
                            leaveRequest.getId(),
                            user,
                            decision
                    )
            );

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
                // 4. TODO: send notifications (Manager)
            }
            case REJECTED -> {
                leaveRequest.setStatus(LeaveRequestStatus.REJECTED);
                leaveRequestRepository.save(leaveRequest);
                // 3.TODO: send notifications (Manager)
            }
        }
        employeeUser.ifPresent(user ->
                notifyFinalDecisionToEmployee(
                        leaveRequest.getId(),
                        user,
                        decision
                )
        );
    }

    private void notifyManagerDecisionToEmployee(Long leaveRequestId, User employeeUser, LeaveRequestDecision decision) {
        String message = switch (decision) {
            case APPROVED -> "Votre demande d'absence n° " + leaveRequestId + " a été acceptée par votre Manager. " +
                    "Elle a été transmise au service des Ressources humaines pour validation définitive.";
            case REJECTED -> "Votre demande d'absence n° " + leaveRequestId + " a été refusée par votre manager. " +
                    "Le service des Ressources humaines prendra prochainement une décision finale.";
        };

        notificationService.create(employeeUser, message);
    }


    private void notifyFinalDecisionToEmployee(Long leaveRequestId, User employeeUser, LeaveRequestDecision decision) {
        String message = switch (decision) {
            case APPROVED -> "Votre demande d'absence n° " + leaveRequestId + " a été acceptée.";
            case REJECTED -> "Votre demande d'absence n° " + leaveRequestId + " a été refusée.";
        };

        notificationService.create(employeeUser, message);
    }

    @Transactional
    public LeaveRequestActionResponseDTO cancel(Long leaveRequestId) {
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

}