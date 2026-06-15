package com.jinji.backend.unit.mapper;

import com.jinji.backend.mapper.EmployeeMapper;
import com.jinji.backend.model.dto.response.EmployeeMeDTO;
import com.jinji.backend.model.entity.Department;
import com.jinji.backend.model.entity.Employee;
import com.jinji.backend.model.entity.Team;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeMapperTest {

    private final EmployeeMapper mapper =
            Mappers.getMapper(EmployeeMapper.class);

    @Test
    void should_map_employee_to_me_dto() {

        Department department = new Department();
        department.setCode("HR");
        department.setName("Human Resources");

        Team team1 = new Team();
        team1.setLabel("Backend");

        Team team2 = new Team();
        team2.setLabel("Frontend");

        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setSurname("Doe");
        employee.setEmail("john@company.com");
        employee.setDepartment(department);
        employee.setTeams(Set.of(team2, team1));

        EmployeeMeDTO dto = mapper.toMeDto(employee);

        assertThat(dto).isNotNull();

        assertThat(dto.departmentName())
                .isEqualTo("Human Resources");

        assertThat(dto.email())
                .isEqualTo("john@company.com");

        assertThat(dto.teams())
                .containsExactly("Backend", "Frontend"); // sorted dans mapper
    }

    @Test
    void should_return_empty_list_when_teams_null() {

        Department department = new Department();
        department.setName("HR");

        Employee employee = new Employee();
        employee.setDepartment(department);
        employee.setTeams(null);

        EmployeeMeDTO dto = mapper.toMeDto(employee);

        assertThat(dto.teams()).isEmpty();
    }
}