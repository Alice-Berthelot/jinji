package com.jinji.backend.unit.service;

import com.jinji.backend.exception.BadRequestException;
import com.jinji.backend.exception.ResourceNotFoundException;
import com.jinji.backend.mapper.EmployeeMapper;
import com.jinji.backend.model.dto.request.EmployeeCreateRequest;
import com.jinji.backend.model.dto.response.EmployeeCreatedDTO;
import com.jinji.backend.model.dto.response.EmployeeProfileDTO;
import com.jinji.backend.model.dto.response.EmployeeTableDTO;
import com.jinji.backend.model.entity.Department;
import com.jinji.backend.model.entity.Employee;
import com.jinji.backend.model.entity.Team;
import com.jinji.backend.model.enums.EmployeeStatus;
import com.jinji.backend.model.enums.RoleEnum;
import com.jinji.backend.model.projection.EmployeeTeamProjection;
import com.jinji.backend.repository.DepartmentRepository;
import com.jinji.backend.repository.EmployeeRepository;
import com.jinji.backend.repository.TeamRepository;
import com.jinji.backend.repository.UserRepository;
import com.jinji.backend.service.business.PermissionService;
import com.jinji.backend.service.crud.EmployeeService;
import com.jinji.backend.service.crud.LeaveBalanceService;
import com.jinji.backend.service.crud.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock private TeamRepository teamRepository;
    @Mock private EmployeeRepository employeeRepository;
    @Mock private DepartmentRepository departmentRepository;
    @Mock private UserRepository userRepository;
    @Mock private UserService userService;
    @Mock private LeaveBalanceService leaveBalanceService;
    @Mock private PermissionService permissionService;
    @Mock private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeService employeeService;

    private Department department;
    private EmployeeCreateRequest request;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setCode("IT");

        request = new EmployeeCreateRequest();
        request.setFirstName("john");
        request.setSurname("doe");
        request.setEmail("JOHN@MAIL.COM");
        request.setDepartmentCode("IT");
        request.setStatus(null);
        request.setPassword("pwd123");
        request.setCreateUser(false);
    }

    // =========================================================
    // CREATE EMPLOYEE
    // =========================================================

    @Nested
    class CreateEmployeeTests {

        @Test
        void should_create_employee_with_external_status_without_leave_balance() {

            request.setStatus(EmployeeStatus.EXTERNAL);

            Employee savedEmployee = new Employee();
            savedEmployee.setId(1L);
            savedEmployee.setStatus(EmployeeStatus.EXTERNAL);

            when(departmentRepository.findByCode("IT"))
                    .thenReturn(Optional.of(department));

            when(employeeRepository.save(any(Employee.class)))
                    .thenReturn(savedEmployee);

            when(employeeMapper.toCreatedDto(savedEmployee))
                    .thenReturn(new EmployeeCreatedDTO(1L, "John", "Doe"));

            employeeService.createEmployee(request);

            verify(leaveBalanceService, never())
                    .createLeaveBalance(any());
        }

        @Test
        void should_throw_when_external_employee_with_user_account() {

            request.setStatus(EmployeeStatus.EXTERNAL);
            request.setCreateUser(true);

            when(departmentRepository.findByCode("IT"))
                    .thenReturn(Optional.of(department));

            Employee savedEmployee = new Employee();
            savedEmployee.setStatus(EmployeeStatus.EXTERNAL);

            when(employeeRepository.save(any(Employee.class)))
                    .thenReturn(savedEmployee);

            assertThatThrownBy(() -> employeeService.createEmployee(request))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("External employees cannot have a user account");

            verifyNoInteractions(userService);
        }

        @Test
        void should_throw_when_create_user_without_password() {

            request.setCreateUser(true);
            request.setPassword(null);

            when(departmentRepository.findByCode("IT"))
                    .thenReturn(Optional.of(department));

            Employee savedEmployee = new Employee();
            savedEmployee.setStatus(EmployeeStatus.INTERNAL);

            when(employeeRepository.save(any(Employee.class)))
                    .thenReturn(savedEmployee);

            assertThatThrownBy(() -> employeeService.createEmployee(request))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("Password is required");

            verify(userService, never())
                    .createUser(any(), any(), any(), any());
        }

        @Test
        void should_create_user_with_employee_role() {

            request.setCreateUser(true);
            request.setPassword("pwd123");

            Employee savedEmployee = new Employee();
            savedEmployee.setId(1L);
            savedEmployee.setEmail("john@mail.com");
            savedEmployee.setStatus(EmployeeStatus.INTERNAL);

            when(departmentRepository.findByCode("IT"))
                    .thenReturn(Optional.of(department));

            when(employeeRepository.save(any(Employee.class)))
                    .thenReturn(savedEmployee);

            when(employeeMapper.toCreatedDto(savedEmployee))
                    .thenReturn(new EmployeeCreatedDTO(1L, "John", "Doe"));

            employeeService.createEmployee(request);

            ArgumentCaptor<Set<RoleEnum>> rolesCaptor =
                    ArgumentCaptor.forClass(Set.class);

            verify(userService).createUser(
                    eq("john@mail.com"),
                    eq("pwd123"),
                    rolesCaptor.capture(),
                    eq(savedEmployee)
            );

            assertThat(rolesCaptor.getValue())
                    .containsExactly(RoleEnum.EMPLOYEE);
        }

        @Test
        void should_add_manager_role_when_manager_team_exists() {

            request.setCreateUser(true);
            request.setPassword("pwd123");
            request.setManagerTeamIds(Set.of(1L));

            Team team = mock(Team.class);

            when(team.getId()).thenReturn(1L);

            Employee savedEmployee = new Employee();
            savedEmployee.setEmail("john@mail.com");
            savedEmployee.setStatus(EmployeeStatus.INTERNAL);

            when(departmentRepository.findByCode("IT"))
                    .thenReturn(Optional.of(department));

            when(employeeRepository.save(any(Employee.class)))
                    .thenReturn(savedEmployee);

            when(teamRepository.findAllByIdIn(any()))
                    .thenReturn(List.of(team));

            when(employeeMapper.toCreatedDto(savedEmployee))
                    .thenReturn(new EmployeeCreatedDTO(1L, "John", "Doe"));

            employeeService.createEmployee(request);

            ArgumentCaptor<Set<RoleEnum>> rolesCaptor =
                    ArgumentCaptor.forClass(Set.class);

            verify(userService).createUser(
                    any(),
                    any(),
                    rolesCaptor.capture(),
                    any()
            );

            assertThat(rolesCaptor.getValue())
                    .contains(RoleEnum.MANAGER);
        }

        @Test
        void should_add_hr_role_for_hr_department() {

            department.setCode("RH");

            request.setCreateUser(true);
            request.setPassword("pwd123");

            Employee savedEmployee = new Employee();
            savedEmployee.setEmail("john@mail.com");
            savedEmployee.setStatus(EmployeeStatus.INTERNAL);

            when(departmentRepository.findByCode("IT"))
                    .thenReturn(Optional.of(department));

            when(employeeRepository.save(any(Employee.class)))
                    .thenReturn(savedEmployee);

            when(employeeMapper.toCreatedDto(savedEmployee))
                    .thenReturn(new EmployeeCreatedDTO(1L, "John", "Doe"));

            employeeService.createEmployee(request);

            ArgumentCaptor<Set<RoleEnum>> rolesCaptor =
                    ArgumentCaptor.forClass(Set.class);

            verify(userService).createUser(
                    any(),
                    any(),
                    rolesCaptor.capture(),
                    any()
            );

            assertThat(rolesCaptor.getValue())
                    .contains(RoleEnum.HR);
        }

        @Test
        void should_assign_employee_as_team_member() {

            Team team = mock(Team.class);

            when(team.getId()).thenReturn(1L);

            request.setMemberTeamIds(Set.of(1L));

            Employee savedEmployee = new Employee();
            savedEmployee.setStatus(EmployeeStatus.INTERNAL);

            when(departmentRepository.findByCode("IT"))
                    .thenReturn(Optional.of(department));

            when(employeeRepository.save(any(Employee.class)))
                    .thenReturn(savedEmployee);

            when(teamRepository.findAllByIdIn(any()))
                    .thenReturn(List.of(team));

            when(employeeMapper.toCreatedDto(savedEmployee))
                    .thenReturn(new EmployeeCreatedDTO(1L, "John", "Doe"));

            employeeService.createEmployee(request);

            verify(team).addEmployee(savedEmployee);
            verify(teamRepository).saveAll(any());
        }

        @Test
        void should_assign_employee_as_team_manager() {

            Team team = mock(Team.class);

            when(team.getId()).thenReturn(1L);

            request.setManagerTeamIds(Set.of(1L));

            Employee savedEmployee = new Employee();
            savedEmployee.setStatus(EmployeeStatus.INTERNAL);

            when(departmentRepository.findByCode("IT"))
                    .thenReturn(Optional.of(department));

            when(employeeRepository.save(any(Employee.class)))
                    .thenReturn(savedEmployee);

            when(teamRepository.findAllByIdIn(any()))
                    .thenReturn(List.of(team));

            when(employeeMapper.toCreatedDto(savedEmployee))
                    .thenReturn(new EmployeeCreatedDTO(1L, "John", "Doe"));

            employeeService.createEmployee(request);

            verify(team).setManager(savedEmployee);
        }

        @Test
        void should_not_add_manager_as_member_when_same_team() {

            Team team = mock(Team.class);

            when(team.getId()).thenReturn(1L);

            request.setMemberTeamIds(Set.of(1L));
            request.setManagerTeamIds(Set.of(1L));

            Employee savedEmployee = new Employee();
            savedEmployee.setStatus(EmployeeStatus.INTERNAL);

            when(departmentRepository.findByCode("IT"))
                    .thenReturn(Optional.of(department));

            when(employeeRepository.save(any(Employee.class)))
                    .thenReturn(savedEmployee);

            when(teamRepository.findAllByIdIn(any()))
                    .thenReturn(List.of(team));

            when(employeeMapper.toCreatedDto(savedEmployee))
                    .thenReturn(new EmployeeCreatedDTO(1L, "John", "Doe"));

            employeeService.createEmployee(request);

            verify(team).setManager(savedEmployee);
            verify(team, never()).addEmployee(savedEmployee);
        }

        @Test
        void should_not_query_teams_when_no_team_ids() {

            Employee savedEmployee = new Employee();
            savedEmployee.setStatus(EmployeeStatus.INTERNAL);

            when(departmentRepository.findByCode("IT"))
                    .thenReturn(Optional.of(department));

            when(employeeRepository.save(any(Employee.class)))
                    .thenReturn(savedEmployee);

            when(employeeMapper.toCreatedDto(savedEmployee))
                    .thenReturn(new EmployeeCreatedDTO(1L, "John", "Doe"));

            employeeService.createEmployee(request);

            verify(teamRepository, never()).findAllByIdIn(any());
            verify(teamRepository, never()).saveAll(any());
        }
    }

    // =========================================================
    // GET EMPLOYEES FOR TABLE
    // =========================================================

    @Nested
    class GetEmployeesForTableTests {

        @Test
        void should_return_employee_table_page() {

            Pageable pageable = PageRequest.of(0, 10);

            EmployeeProfileDTO profile = mock(EmployeeProfileDTO.class);
            when(profile.id()).thenReturn(1L);
            when(profile.employeeNumber()).thenReturn("EMP001");
            when(profile.surname()).thenReturn("Doe");
            when(profile.firstName()).thenReturn("John");
            when(profile.email()).thenReturn("john@mail.com");
            when(profile.phoneNumber()).thenReturn("0600000000");
            when(profile.seniorityDate()).thenReturn(LocalDate.now());
            when(profile.status()).thenReturn(EmployeeStatus.INTERNAL);
            when(profile.departmentName()).thenReturn("IT");

            Page<EmployeeProfileDTO> page =
                    new PageImpl<>(List.of(profile));

            when(employeeRepository.findEmployeesForTable(null, pageable))
                    .thenReturn(page);

            EmployeeTeamProjection projection = mock(EmployeeTeamProjection.class);
            when(projection.getEmployeeId()).thenReturn(1L);
            when(projection.getLabel()).thenReturn("Backend");

            when(employeeRepository.findTeamNamesByEmployeeIds(List.of(1L)))
                    .thenReturn(List.of(projection));

            EmployeeTableDTO expected =
                    new EmployeeTableDTO(
                            1L,
                            "EMP001",
                            "Doe",
                            "John",
                            "john@mail.com",
                            "0600000000",
                            LocalDate.now(),
                            EmployeeStatus.INTERNAL,
                            "IT",
                            List.of("Backend")
                    );

            Page<EmployeeTableDTO> result =
                    employeeService.getEmployeesForTable(null, pageable);

            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);

            verify(employeeRepository).findEmployeesForTable(null, pageable);
            verify(employeeRepository).findTeamNamesByEmployeeIds(List.of(1L));
        }

        @Test
        void should_return_empty_page_without_loading_teams() {

            Pageable pageable = PageRequest.of(0, 10);

            Page<EmployeeProfileDTO> emptyPage =
                    new PageImpl<>(List.of());

            when(employeeRepository.findEmployeesForTable(null, pageable))
                    .thenReturn(emptyPage);

            Page<EmployeeTableDTO> result =
                    employeeService.getEmployeesForTable(null, pageable);

            assertThat(result.getContent()).isEmpty();

            verify(employeeRepository, never())
                    .findTeamNamesByEmployeeIds(any());
        }

        @Test
        void should_return_employee_without_teams() {

            Pageable pageable = PageRequest.of(0, 10);

            EmployeeProfileDTO profile = mock(EmployeeProfileDTO.class);

            when(profile.id()).thenReturn(1L);
            when(profile.employeeNumber()).thenReturn("EMP001");
            when(profile.surname()).thenReturn("Doe");
            when(profile.firstName()).thenReturn("John");
            when(profile.email()).thenReturn("john@mail.com");
            when(profile.phoneNumber()).thenReturn("0600000000");
            when(profile.seniorityDate()).thenReturn(LocalDate.now());
            when(profile.status()).thenReturn(EmployeeStatus.INTERNAL);
            when(profile.departmentName()).thenReturn("IT");

            Page<EmployeeProfileDTO> page =
                    new PageImpl<>(List.of(profile));

            when(employeeRepository.findEmployeesForTable(null, pageable))
                    .thenReturn(page);

            when(employeeRepository.findTeamNamesByEmployeeIds(List.of(1L)))
                    .thenReturn(List.of());

            Page<EmployeeTableDTO> result =
                    employeeService.getEmployeesForTable(null, pageable);

            assertThat(result.getContent())
                    .hasSize(1);

            assertThat(result.getContent().get(0).teams())
                    .isEmpty();
        }
    }
}