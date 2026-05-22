package com.jinji.backend.mapper;

import com.jinji.backend.model.dto.EmployeeHrDTO;
import com.jinji.backend.model.dto.EmployeeManagerDTO;
import com.jinji.backend.model.dto.EmployeeMeDTO;
import com.jinji.backend.model.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(source = "department.code", target = "departmentCode")
    EmployeeMeDTO toMeDto(Employee employee);

//    @Mapping(source = "fullName", target = "fullName")
//    @Mapping(source = "department.code", target = "departmentCode")
//    EmployeeManagerDTO toManagerDto(Employee employee);
//
//    @Mapping(source = "department.code", target = "departmentCode")
//    EmployeeHrDTO toHrDto(Employee employee);
//
//    List<EmployeeManagerDTO> toManagerDtos(List<Employee> employees);
//
//    List<EmployeeHrDTO> toHrDtos(List<Employee> employees);
}