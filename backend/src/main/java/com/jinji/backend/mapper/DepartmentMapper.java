package com.jinji.backend.mapper;

import com.jinji.backend.model.dto.response.DepartmentDTO;
import com.jinji.backend.model.entity.Department;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    DepartmentDTO toDto(Department department);

    List<DepartmentDTO> toDtos(List<Department> departments);
}