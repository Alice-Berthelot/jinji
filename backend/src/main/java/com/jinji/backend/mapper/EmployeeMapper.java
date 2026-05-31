package com.jinji.backend.mapper;

import com.jinji.backend.model.dto.EmployeeDetailsDTO;
import com.jinji.backend.model.dto.EmployeeHrDTO;
import com.jinji.backend.model.dto.EmployeeManagerDTO;
import com.jinji.backend.model.dto.EmployeeMeDTO;
import com.jinji.backend.model.dto.response.EmployeeCreatedDTO;
import com.jinji.backend.model.entity.Employee;
import com.jinji.backend.model.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(source = "department.code", target = "departmentCode")
    EmployeeMeDTO toMeDto(Employee employee);

    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "teams", target = "teams")
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

    EmployeeCreatedDTO toCreatedDto(Employee employee);
}