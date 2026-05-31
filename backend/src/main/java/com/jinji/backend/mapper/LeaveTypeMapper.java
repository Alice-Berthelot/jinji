package com.jinji.backend.mapper;

import com.jinji.backend.model.dto.LeaveTypeDTO;
import com.jinji.backend.model.entity.LeaveType;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LeaveTypeMapper {

    LeaveTypeDTO toDto(LeaveType leaveType);

    List<LeaveTypeDTO> toDtoList(List<LeaveType> leaveTypes);
}