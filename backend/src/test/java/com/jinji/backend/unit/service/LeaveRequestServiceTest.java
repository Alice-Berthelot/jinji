package com.jinji.backend.unit.service;

import com.jinji.backend.exception.BadRequestException;
import com.jinji.backend.exception.ForbiddenException;
import com.jinji.backend.exception.ResourceNotFoundException;
import com.jinji.backend.mapper.LeaveRequestMapper;
import com.jinji.backend.model.dto.request.LeaveRequestCreateRequest;
import com.jinji.backend.model.dto.request.LeaveRequestCreateReview;
import com.jinji.backend.model.dto.response.LeaveRequestDTO;
import com.jinji.backend.model.entity.*;
import com.jinji.backend.model.enums.*;
import com.jinji.backend.repository.LeaveRequestRepository;
import com.jinji.backend.repository.LeaveRequestReviewRepository;
import com.jinji.backend.repository.LeaveTypeRepository;
import com.jinji.backend.service.business.LeaveCalculationService;
import com.jinji.backend.service.business.PermissionService;
import com.jinji.backend.service.crud.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeaveRequestServiceTest {

    @Mock private LeaveRequestRepository leaveRequestRepository;
    @Mock private LeaveTypeRepository leaveTypeRepository;
    @Mock private LeaveRequestReviewRepository leaveRequestReviewRepository;
    @Mock private UserService userService;
    @Mock private EmployeeService employeeService;
    @Mock private HrPolicyService hrPolicyService;
    @Mock private LeaveCalculationService leaveCalculationService;
    @Mock private LeaveRequestReviewService leaveRequestReviewService;
    @Mock private LeaveService leaveService;
    @Mock private TeamService teamService;
    @Mock private NotificationService notificationService;
    @Mock private PermissionService permissionService;
    @Mock private LeaveRequestMapper leaveRequestMapper;

    @InjectMocks
    private LeaveRequestService leaveRequestService;

    private Employee authEmployee;
    private Employee targetEmployee;
    private User currentUser;
    private LeaveType leaveType;
    private LeaveRequestCreateRequest request;

    @BeforeEach
    void setUp() {

        authEmployee = new Employee();
        authEmployee.setId(1L);
        authEmployee.setFirstName("John");
        authEmployee.setSurname("Manager");
        authEmployee.setStatus(EmployeeStatus.INTERNAL);
        targetEmployee = new Employee();
        targetEmployee.setId(2L);

        currentUser = new User();
        currentUser.setId(1L);
        currentUser.setEmployee(authEmployee);
        currentUser.setRoles(Set.of());

        leaveType = new LeaveType();
        leaveType.setId(1L);
        leaveType.setCode("CP");
        leaveType.setLabel("Congés payés");
        leaveType.setBalanceManaged(true);

        request = new LeaveRequestCreateRequest();
        request.setLeaveTypeCode("CP");
        request.setStartDate(LocalDate.of(2025, 1, 1));
        request.setEndDate(LocalDate.of(2025, 1, 5));
        request.setStartPeriod(PeriodType.AM);
        request.setEndPeriod(PeriodType.PM);
        request.setEmployeeComment("Vacances");
    }

    private Role role(RoleEnum roleEnum) {
        Role role = new Role();
        role.setCode(roleEnum);
        role.setLabel(roleEnum.name());
        return role;
    }

    // =========================================================
    // CREATE LEAVE REQUEST
    // =========================================================
    @Nested
    class CreateLeaveRequestTests {

        @Test
        void should_create_leave_request_successfully() {

            when(employeeService.getCurrentEmployee()).thenReturn(authEmployee);
            when(leaveTypeRepository.findByCode("CP")).thenReturn(Optional.of(leaveType));
            when(leaveCalculationService.calculateLeaveDays(any(), any(), any(), any()))
                    .thenReturn(BigDecimal.valueOf(5));
            when(userService.getCurrentUser()).thenReturn(currentUser);
            when(leaveRequestRepository.save(any(LeaveRequest.class)))
                    .thenAnswer(inv -> {
                        LeaveRequest lr = inv.getArgument(0);
                        lr.setId(10L);
                        return lr;
                    });
            when(leaveRequestMapper.toDto(any())).thenReturn(mock(LeaveRequestDTO.class));

            LeaveRequestDTO result = leaveRequestService.createLeaveRequest(request);

            assertThat(result).isNotNull();
            verify(leaveRequestRepository).save(any(LeaveRequest.class));
        }

        @Test
        void should_throw_when_no_employee_linked() {

            when(employeeService.getCurrentEmployee()).thenReturn(null);

            assertThatThrownBy(() ->
                    leaveRequestService.createLeaveRequest(request)
            ).isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        void should_throw_when_not_internal_employee() {

            authEmployee.setStatus(EmployeeStatus.EXTERNAL);
            when(employeeService.getCurrentEmployee()).thenReturn(authEmployee);

            assertThatThrownBy(() ->
                    leaveRequestService.createLeaveRequest(request)
            ).isInstanceOf(ForbiddenException.class);
        }
    }

    // =========================================================
    // PROCESS LEAVE REQUEST
    // =========================================================
    @Nested
    class ProcessLeaveRequestTests {

        @Test
        void should_throw_when_leave_request_not_found() {

            when(leaveRequestRepository.findById(1L))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() ->
                    leaveRequestService.processLeaveRequest(
                            1L,
                            new LeaveRequestCreateReview()
                    )
            ).isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        void should_throw_when_owner_try_to_process_own_request() {

            LeaveRequest lr = new LeaveRequest();
            lr.setId(1L);
            lr.setEmployee(authEmployee);
            lr.setStatus(LeaveRequestStatus.PENDING);

            when(leaveRequestRepository.findById(1L))
                    .thenReturn(Optional.of(lr));

            when(userService.getCurrentUser()).thenReturn(currentUser);

            assertThatThrownBy(() ->
                    leaveRequestService.processLeaveRequest(
                            1L,
                            new LeaveRequestCreateReview()
                    )
            ).isInstanceOf(RuntimeException.class);
        }

        @Test
        void should_throw_when_already_processed() {

            LeaveRequest lr = new LeaveRequest();
            lr.setId(1L);
            lr.setEmployee(targetEmployee);
            lr.setStatus(LeaveRequestStatus.APPROVED);

            when(leaveRequestRepository.findById(1L))
                    .thenReturn(Optional.of(lr));

            assertThatThrownBy(() ->
                    leaveRequestService.processLeaveRequest(
                            1L,
                            new LeaveRequestCreateReview()
                    )
            ).isInstanceOf(BadRequestException.class);
        }
    }
}