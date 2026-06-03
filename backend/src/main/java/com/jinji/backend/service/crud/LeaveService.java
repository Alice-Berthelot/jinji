package com.jinji.backend.service.crud;

import com.jinji.backend.exception.BadRequestException;
import com.jinji.backend.exception.ForbiddenException;
import com.jinji.backend.exception.ResourceNotFoundException;
import com.jinji.backend.mapper.LeaveMapper;
import com.jinji.backend.model.dto.*;
import com.jinji.backend.model.dto.request.LeaveCreateRequest;
import com.jinji.backend.model.dto.response.MyLeaveCalendarDTO;
import com.jinji.backend.model.entity.*;
import com.jinji.backend.model.enums.EmployeePageView;
import com.jinji.backend.model.enums.LeaveStatus;
import com.jinji.backend.model.enums.PeriodType;
import com.jinji.backend.model.projection.LeaveCalendarProjection;
import com.jinji.backend.repository.EmployeeRepository;
import com.jinji.backend.repository.LeaveRepository;
import com.jinji.backend.repository.LeaveTypeRepository;
import com.jinji.backend.repository.projection.LeaveRaw;
import com.jinji.backend.service.business.LeaveCalculationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeaveService {

    private final LeaveRepository leaveRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveBalanceService leaveBalanceService;
    private final LeaveCalculationService leaveCalculationService;
    private final UserService userService;
    private final NotificationService notificationService;
    private final LeaveMapper leaveMapper;

    public LeaveService(
            LeaveRepository leaveRepository, LeaveTypeRepository leaveTypeRepository, EmployeeRepository employeeRepository, LeaveBalanceService leaveBalanceService, LeaveCalculationService leaveCalculationService, UserService userService, NotificationService notificationService, LeaveMapper leaveMapper
    ) {
        this.leaveRepository = leaveRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.employeeRepository = employeeRepository;
        this.leaveBalanceService = leaveBalanceService;
        this.leaveCalculationService = leaveCalculationService;
        this.userService = userService;
        this.notificationService = notificationService;
        this.leaveMapper = leaveMapper;
    }

    @Transactional
    public LeaveDTO createLeave(LeaveCreateRequest request) {
        User currentUser = userService.getCurrentUser();
        boolean isHr = currentUser.isHr();
        Employee employeeAuth = currentUser.getEmployee();
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        if ((employeeAuth.getId().equals(employee.getId())) && !isHr) {
            throw new ForbiddenException("User is not authorized to create a leave for employee " + employee.getFullName());
        }

        LeaveType leaveType = leaveTypeRepository
                .findByCode(request.getLeaveTypeCode())
                .orElseThrow(() -> new ResourceNotFoundException(
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
        leaveRaw.setLeaveType(leaveType);

        BigDecimal numberOfDays = BigDecimal.ZERO;

        if (leaveRaw.getLeaveType().isBalanceManaged()) {
            if ("CP".equals(request.getLeaveTypeCode())) {
                numberOfDays = leaveCalculationService.calculateLeaveDays(
                        request.getStartDate(),
                        request.getEndDate(),
                        startPeriod,
                        endPeriod
                );
            }
        }

        leaveRaw.setNumberOfDays(numberOfDays);


        Leave leave = buildLeave(leaveRaw);


        userService.findByEmployeeId(leave.getEmployee().getId())
                .ifPresent(user ->
                        notificationService.create(
                                user,
                                "Une absence a été ajoutée à votre compte. Vous pouvez la consulter dans votre espace personnel."
                        )
                );

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

        leave.setNumberOfDays(leaveRaw.getNumberOfDays());

        leaveRepository.save(leave);

        if (leave.getLeaveType().isBalanceManaged()) {
            leaveBalanceService.deductLeaveBalance(leave.getEmployee(), leave.getLeaveType(), leaveRaw.getNumberOfDays());
        }

        return leave;
    }

    @Transactional(readOnly = true)
    public List<MyLeaveCalendarDTO> getAllMyLeaves() {

        Employee me = userService.getCurrentUser().getEmployee();

        return leaveRepository.findEmployeeCalendar(me.getId())
                .stream()
                .map(p -> new MyLeaveCalendarDTO(
                        new LeaveTypeDTO(p.getLeaveTypeCode(), p.getLeaveTypeLabel()),
                        p.getStartDate(),
                        p.getEndDate(),
                        p.getLeaveId(),
                        p.getStatus()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LeaveCalendarDTO> getAllLeaves(EmployeePageView pageType) {

        User currentUser = userService.getCurrentUser();
        Employee authEmployee = currentUser.getEmployee();
        if (authEmployee == null) {
            throw new ResourceNotFoundException("No employee linked to this user");
        }

        validateAccess(pageType, currentUser);

        List<LeaveCalendarProjection> data =
                switch (pageType) {
                    case HR -> leaveRepository.findHrCalendar();
                    case MANAGER -> leaveRepository.findManagerCalendar(authEmployee.getId());
                };

        return data.stream()
                .map(p -> new LeaveCalendarDTO(
                        p.getEmployeeId(),
                        p.getFirstName(),
                        p.getSurname(),
                        new LeaveTypeDTO(
                                p.getLeaveTypeCode(),
                                p.getLeaveTypeLabel()
                        ),
                        p.getStartDate(),
                        p.getEndDate(),
                        p.getLeaveId()
                ))
                .toList();
    }

    private void validateAccess(EmployeePageView pageType, User user) {

        switch (pageType) {
            case HR -> {
                if (!user.isHr()) {
                    throw new ForbiddenException("User not authorized");
                }
            }
            case MANAGER -> {
                if (!user.isManager()) {
                    throw new ForbiddenException("User not authorized");
                }
            }
        }
    }

    @Transactional
    public LeaveDTO cancelLeave(Long leaveId) {

        User currentUser = userService.getCurrentUser();

        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Leave not found with id " + leaveId)
                );

        authorizeLeaveCancellation(currentUser, leave);

        if (leave.getStatus() == LeaveStatus.CANCELLED) {
            throw new BadRequestException("Leave has already been cancelled");
        }

        leave.setStatus(LeaveStatus.CANCELLED);

        leaveRepository.save(leave);

        if ("CP".equals(leave.getLeaveType().getCode())
                && leave.getNumberOfDays() != null) {

            leaveBalanceService.creditLeaveBalance(
                    leave.getEmployee(),
                    leave.getLeaveType(),
                    leave.getNumberOfDays()
            );
        }

        userService.findByEmployeeId(leave.getEmployee().getId())
                .ifPresent(user ->
                        notificationService.create(
                                user,
                                "Votre absence n°" + leave.getId() + " a été annulée."
                        )
                );

        return leaveMapper.toDto(leave);
    }

    private void authorizeLeaveCancellation(User user, Leave leave) {

        Employee currentEmployee = user.getEmployee();

        if (currentEmployee == null && !user.isHr()) {
            throw new ForbiddenException("Not authorized");
        }

        boolean isOwner = currentEmployee != null
                && leave.getCreatedBy().getId().equals(currentEmployee.getId());

        boolean isHr = user.isHr();

        if (!isOwner && !isHr) {
            throw new ForbiddenException("User is not authorized to cancel leave n°" + leave.getId());
        }
    }
}