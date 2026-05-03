package com.jinji.backend.service;

import com.jinji.backend.model.dto.LeaveTypeDTO;
import com.jinji.backend.repository.LeaveTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveTypeService {

    private final LeaveTypeRepository leaveTypeRepository;

    public LeaveTypeService(LeaveTypeRepository leaveTypeRepository) {
        this.leaveTypeRepository = leaveTypeRepository;
    }

    public List<LeaveTypeDTO> getAllLeaveTypes() {
        return leaveTypeRepository.findAll()
                .stream()
                .map(type -> new LeaveTypeDTO(
                        type.getCode(),
                        type.getLabel()
                ))
                .toList();
    }
}