package com.jinji.backend.service.crud;

import com.jinji.backend.mapper.DepartmentMapper;
import com.jinji.backend.model.dto.DepartmentDTO;
import com.jinji.backend.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentService(
            DepartmentRepository departmentRepository,
            DepartmentMapper departmentMapper
    ) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    public List<DepartmentDTO> getAllDepartments() {

        return departmentMapper.toDtos(
                departmentRepository.findAll()
        );
    }
}