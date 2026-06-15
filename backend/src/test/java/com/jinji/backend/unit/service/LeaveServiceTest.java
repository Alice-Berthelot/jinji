package com.jinji.backend.unit.service;

import com.jinji.backend.exception.ForbiddenException;
import com.jinji.backend.exception.ResourceNotFoundException;
import com.jinji.backend.mapper.LeaveMapper;
import com.jinji.backend.model.dto.request.LeaveCreateRequest;
import com.jinji.backend.model.dto.response.LeaveDTO;
import com.jinji.backend.model.entity.*;
import com.jinji.backend.model.enums.PeriodType;
import com.jinji.backend.model.enums.RoleEnum;
import com.jinji.backend.repository.EmployeeRepository;
import com.jinji.backend.repository.LeaveRepository;
import com.jinji.backend.repository.LeaveTypeRepository;
import com.jinji.backend.service.business.LeaveCalculationService;
import com.jinji.backend.service.business.PermissionService;
import com.jinji.backend.service.crud.LeaveBalanceService;
import com.jinji.backend.service.crud.LeaveService;
import com.jinji.backend.service.crud.NotificationService;
import com.jinji.backend.service.crud.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class LeaveServiceTest {

    @Mock
    private LeaveRepository leaveRepository;
    @Mock private LeaveTypeRepository leaveTypeRepository;
    @Mock private EmployeeRepository employeeRepository;
    @Mock private LeaveBalanceService leaveBalanceService;
    @Mock private LeaveCalculationService leaveCalculationService;
    @Mock private UserService userService;
    @Mock private NotificationService notificationService;
    @Mock private PermissionService permissionService;
    @Mock private LeaveMapper leaveMapper;

    @InjectMocks
    private LeaveService leaveService;

    private User currentUser;
    private Employee authEmployee;
    private Employee targetEmployee;
    private LeaveType leaveType;
    private LeaveCreateRequest request;
    private Role role(RoleEnum roleEnum) {
        Role role = new Role();
        role.setCode(roleEnum);
        role.setLabel(roleEnum.name());
        return role;
    }

    @BeforeEach
    void setUp() {

        authEmployee = new Employee();
        authEmployee.setId(100L);
        authEmployee.setFirstName("John");
        authEmployee.setSurname("Manager");

        targetEmployee = new Employee();
        targetEmployee.setId(200L);
        targetEmployee.setFirstName("Jane");
        targetEmployee.setSurname("Doe");

        currentUser = new User();
        currentUser.setId(1L);
        currentUser.setEmployee(authEmployee);
        currentUser.setRoles(Set.of(role(RoleEnum.EMPLOYEE)));

        leaveType = new LeaveType();
        leaveType.setId(1L);
        leaveType.setCode("CP");
        leaveType.setLabel("Congés payés");
        leaveType.setBalanceManaged(true);

        request = new LeaveCreateRequest();
        request.setEmployeeId(200L);
        request.setLeaveTypeCode("CP");
        request.setStartDate(LocalDate.of(2025, 1, 1));
        request.setEndDate(LocalDate.of(2025, 1, 5));
        request.setStartPeriod(PeriodType.AM);
        request.setEndPeriod(PeriodType.PM);
    }

    @Nested
    class CreateLeaveTests {

        @Test
        void should_throw_when_employee_not_found() {

            when(userService.getCurrentUser())
                    .thenReturn(currentUser);

            when(employeeRepository.findById(200L))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> leaveService.createLeave(request))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Employee not found");
        }

        @Test
        void should_throw_when_employee_creates_his_own_leave() {

            currentUser.setRoles(Set.of(role(RoleEnum.EMPLOYEE)));

            authEmployee.setId(100L);

            request.setEmployeeId(100L);

            when(userService.getCurrentUser())
                    .thenReturn(currentUser);

            when(employeeRepository.findById(100L))
                    .thenReturn(Optional.of(authEmployee));

            assertThatThrownBy(() -> leaveService.createLeave(request))
                    .isInstanceOf(ForbiddenException.class)
                    .hasMessageContaining("not authorized");
        }

        @Test
        void should_allow_hr_to_create_leave_for_himself() {

            currentUser.setRoles(Set.of(role(RoleEnum.HR)));

            request.setEmployeeId(100L);

            when(userService.getCurrentUser())
                    .thenReturn(currentUser);

            when(employeeRepository.findById(100L))
                    .thenReturn(Optional.of(authEmployee));

            when(leaveTypeRepository.findByCode("CP"))
                    .thenReturn(Optional.of(leaveType));

            when(leaveMapper.toDto(any()))
                    .thenReturn(mock(LeaveDTO.class));

            leaveService.createLeave(request);

            verify(leaveRepository).save(any(Leave.class));
        }

        @Test
        void should_throw_when_leave_type_not_found() {

            when(userService.getCurrentUser())
                    .thenReturn(currentUser);

            when(employeeRepository.findById(200L))
                    .thenReturn(Optional.of(targetEmployee));

            when(leaveTypeRepository.findByCode("CP"))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> leaveService.createLeave(request))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Leave type not found");
        }

        @Test
        void should_calculate_days_for_cp_leave() {

            when(userService.getCurrentUser())
                    .thenReturn(currentUser);

            when(employeeRepository.findById(200L))
                    .thenReturn(Optional.of(targetEmployee));

            when(leaveTypeRepository.findByCode("CP"))
                    .thenReturn(Optional.of(leaveType));

            when(leaveCalculationService.calculateLeaveDays(
                    any(),
                    any(),
                    any(),
                    any()
            )).thenReturn(BigDecimal.valueOf(5));

            when(leaveMapper.toDto(any()))
                    .thenReturn(mock(LeaveDTO.class));

            leaveService.createLeave(request);

            verify(leaveCalculationService)
                    .calculateLeaveDays(
                            any(),
                            any(),
                            any(),
                            any()
                    );
        }

        @Test
        void should_deduct_leave_balance_when_balance_managed() {

            when(userService.getCurrentUser())
                    .thenReturn(currentUser);

            when(employeeRepository.findById(200L))
                    .thenReturn(Optional.of(targetEmployee));

            when(leaveTypeRepository.findByCode("CP"))
                    .thenReturn(Optional.of(leaveType));

            when(leaveCalculationService.calculateLeaveDays(
                    any(),
                    any(),
                    any(),
                    any()
            )).thenReturn(BigDecimal.valueOf(5));

            when(leaveMapper.toDto(any()))
                    .thenReturn(mock(LeaveDTO.class));

            leaveService.createLeave(request);

            verify(leaveBalanceService)
                    .deductLeaveBalance(
                            eq(targetEmployee),
                            eq(leaveType),
                            eq(BigDecimal.valueOf(5))
                    );
        }

        @Test
        void should_not_deduct_leave_balance_when_not_balance_managed() {

            leaveType.setBalanceManaged(false);

            when(userService.getCurrentUser())
                    .thenReturn(currentUser);

            when(employeeRepository.findById(200L))
                    .thenReturn(Optional.of(targetEmployee));

            when(leaveTypeRepository.findByCode("CP"))
                    .thenReturn(Optional.of(leaveType));

            when(leaveMapper.toDto(any()))
                    .thenReturn(mock(LeaveDTO.class));

            leaveService.createLeave(request);

            verify(leaveBalanceService, never())
                    .deductLeaveBalance(any(), any(), any());
        }

        @Test
        void should_create_notification_when_employee_has_user() {

            User employeeUser = new User();

            when(userService.getCurrentUser())
                    .thenReturn(currentUser);

            when(employeeRepository.findById(200L))
                    .thenReturn(Optional.of(targetEmployee));

            when(leaveTypeRepository.findByCode("CP"))
                    .thenReturn(Optional.of(leaveType));

            when(userService.findByEmployeeId(200L))
                    .thenReturn(Optional.of(employeeUser));

            when(leaveMapper.toDto(any()))
                    .thenReturn(mock(LeaveDTO.class));

            leaveService.createLeave(request);

            verify(notificationService)
                    .create(
                            eq(employeeUser),
                            contains("absence")
                    );
        }

        @Test
        void should_not_create_notification_when_employee_has_no_user() {

            when(userService.getCurrentUser())
                    .thenReturn(currentUser);

            when(employeeRepository.findById(200L))
                    .thenReturn(Optional.of(targetEmployee));

            when(leaveTypeRepository.findByCode("CP"))
                    .thenReturn(Optional.of(leaveType));

            when(userService.findByEmployeeId(200L))
                    .thenReturn(Optional.empty());

            when(leaveMapper.toDto(any()))
                    .thenReturn(mock(LeaveDTO.class));

            leaveService.createLeave(request);

            verify(notificationService, never())
                    .create(any(), any());
        }
    }

    @Nested
    class GetMyLeavesTests { }

    @Nested
    class GetAllLeavesTests { }

    @Nested
    class CancelLeaveTests { }
}