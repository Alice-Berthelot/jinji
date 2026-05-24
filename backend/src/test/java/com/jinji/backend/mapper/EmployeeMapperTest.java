package com.jinji.backend.mapper;

import com.jinji.backend.model.dto.EmployeeMeDTO;
import com.jinji.backend.model.entity.Department;
import com.jinji.backend.model.entity.Employee;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeMapperTest {

    private final EmployeeMapper mapper =
            Mappers.getMapper(EmployeeMapper.class);

    @Test
    void should_map_employee_to_me_dto() {

        Department department = new Department();
        department.setCode("HR");

        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setSurname("Doe");
        employee.setEmail("john@company.com");
        employee.setDepartment(department);

        EmployeeMeDTO dto = mapper.toMeDto(employee);

        assertThat(dto).isNotNull();
        assertThat(dto.getDepartmentCode()).isEqualTo("HR");
        assertThat(dto.getEmail()).isEqualTo("john@company.com");
    }
}