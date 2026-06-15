package com.jinji.backend.mapper;

import com.jinji.backend.model.dto.response.EmployeeDetailsDTO;
import com.jinji.backend.model.dto.response.EmployeeMeDTO;
import com.jinji.backend.model.dto.response.EmployeeCreatedDTO;
import com.jinji.backend.model.entity.Employee;
import com.jinji.backend.model.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "teams", target = "teams")
    @Mapping(source = "status", target = "status")
    EmployeeMeDTO toMeDto(Employee employee);

    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "teams", target = "teams")
    @Mapping(source = "status", target = "status")
    EmployeeDetailsDTO toDetailsDto(Employee employee);
    default List<String> mapTeams(Set<Team> teams) {
        if (teams == null) {
            return List.of();
        }

        return teams.stream()
                .map(Team::getLabel)
                .sorted()
                .toList();
    }

    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "status", target = "status")
    @Mapping(target = "employeeNumber", ignore = true)
    @Mapping(target = "phoneNumber", ignore = true)
    @Mapping(target = "teams", ignore = true)
    EmployeeDetailsDTO toManagerDetailsDto(Employee employee);

    EmployeeCreatedDTO toCreatedDto(Employee employee);
}