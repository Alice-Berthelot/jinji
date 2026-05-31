package com.jinji.backend.service.crud;

import com.jinji.backend.mapper.LeaveTypeMapper;
import com.jinji.backend.model.dto.LeaveTypeDTO;
import com.jinji.backend.repository.LeaveTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveTypeService {

    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveTypeMapper leaveTypeMapper;

    public LeaveTypeService(LeaveTypeRepository leaveTypeRepository, LeaveTypeMapper leaveTypeMapper) {
        this.leaveTypeRepository = leaveTypeRepository;
        this.leaveTypeMapper = leaveTypeMapper;
    }

    public List<LeaveTypeDTO> getAllLeaveTypes() {
        return leaveTypeMapper.toDtoList(
                leaveTypeRepository.findAll()
        );
    }

    public List<LeaveTypeDTO> getRequestableLeaveTypes() {
        return leaveTypeMapper.toDtoList(
                leaveTypeRepository.findByRequestableTrue()
        );
    }
}