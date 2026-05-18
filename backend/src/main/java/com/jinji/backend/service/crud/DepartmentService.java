package com.jinji.backend.service.crud;

import com.jinji.backend.model.dto.DepartmentResponse;
import com.jinji.backend.model.entity.Department;
import com.jinji.backend.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<DepartmentResponse> getAllDepartments() {

        return departmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private DepartmentResponse mapToResponse(Department department) {
        DepartmentResponse departmentResponse = new DepartmentResponse();
        departmentResponse.setId(department.getId());
        departmentResponse.setCode(department.getCode());
        departmentResponse.setName(department.getName());
        return departmentResponse;
    }
}
