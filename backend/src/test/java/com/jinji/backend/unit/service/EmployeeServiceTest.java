package com.jinji.backend.unit.service;

import com.jinji.backend.exception.ResourceNotFoundException;
import com.jinji.backend.mapper.EmployeeMapper;
import com.jinji.backend.model.dto.request.EmployeeCreateRequest;
import com.jinji.backend.model.dto.response.EmployeeCreatedDTO;
import com.jinji.backend.model.entity.Department;
import com.jinji.backend.model.entity.Employee;
import com.jinji.backend.model.entity.User;
import com.jinji.backend.repository.DepartmentRepository;
import com.jinji.backend.repository.EmployeeRepository;
import com.jinji.backend.repository.UserRepository;
import com.jinji.backend.service.crud.EmployeeService;
import com.jinji.backend.service.crud.LeaveBalanceService;
import com.jinji.backend.service.crud.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Employee service tests")
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private LeaveBalanceService leaveBalanceService;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeService employeeService;

    private EmployeeCreateRequest request;
    private Department department;

    @BeforeEach
    void setUp() {

        department = new Department();
        department.setCode("IT");

        request = new EmployeeCreateRequest();
        request.setFirstName("john");
        request.setSurname("doe");
        request.setEmail("JOHN@MAIL.COM ");
        request.setDepartmentCode("IT");
    }

    @Nested
    @DisplayName("Create employee tests")
    class CreateEmployeeTests {

        @Test
        @DisplayName("Should create an employee successfully")
        void should_create_employee() {

            Employee savedEmployee = new Employee();
            savedEmployee.setFirstName("John");
            savedEmployee.setSurname("Doe");
            savedEmployee.setEmail("john@mail.com");

            EmployeeCreatedDTO expectedDto =
                    new EmployeeCreatedDTO(1L, "John", "Doe");

            when(departmentRepository.findByCode("IT"))
                    .thenReturn(Optional.of(department));

            when(employeeRepository.save(any(Employee.class)))
                    .thenReturn(savedEmployee);

            when(employeeMapper.toCreatedDto(any(Employee.class)))
                    .thenReturn(expectedDto);

            EmployeeCreatedDTO result =
                    employeeService.createEmployee(request);

            assertThat(result).isNotNull();
            assertThat(result.firstName()).isEqualTo("John");
            assertThat(result.surname()).isEqualTo("Doe");

            verify(employeeRepository)
                    .save(any(Employee.class));

            verify(employeeMapper)
                    .toCreatedDto(any(Employee.class));

            verify(leaveBalanceService)
                    .createLeaveBalance(any(Employee.class));
        }

        @Test
        @DisplayName("Should throw exception when department is not found")
        void should_throw_when_department_not_found() {

            when(departmentRepository.findByCode("IT"))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() ->
                    employeeService.createEmployee(request)
            )
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Department not found with code: IT");

            verify(employeeRepository, never())
                    .save(any(Employee.class));
        }
    }

    @Nested
    @DisplayName("Get current employee tests")
    class GetCurrentEmployeeTests {

        @Test
        @DisplayName("Should return current employee")
        void should_return_current_employee() {

            Employee employee = new Employee();
            employee.setFirstName("John");

            User user = new User();
            user.setEmployee(employee);

            when(userRepository.findByUsername("john"))
                    .thenReturn(Optional.of(user));

            Employee result =
                    employeeService.getCurrentEmployee("john");

            assertThat(result).isNotNull();
            assertThat(result.getFirstName())
                    .isEqualTo("John");
        }

        @Test
        @DisplayName("Should throw exception when user is not found")
        void should_throw_when_user_not_found() {

            when(userRepository.findByUsername("unknown"))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() ->
                    employeeService.getCurrentEmployee("unknown")
            )
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("User not found: unknown");
        }

        @Test
        @DisplayName("Should throw exception when employee is not linked to user")
        void should_throw_when_employee_not_linked() {

            User user = new User();
            user.setEmployee(null);

            when(userRepository.findByUsername("john"))
                    .thenReturn(Optional.of(user));

            assertThatThrownBy(() ->
                    employeeService.getCurrentEmployee("john")
            )
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("No employee linked to this user");
        }
    }
}