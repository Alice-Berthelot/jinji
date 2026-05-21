package com.jinji.backend.mapper;

import com.jinji.backend.model.dto.LeaveDTO;
import com.jinji.backend.model.entity.Leave;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LeaveMapper {

    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.fullName", target = "employeeFullName")

    @Mapping(source = "leaveType.label", target = "leaveTypeLabel")

    @Mapping(source = "leaveRequest.id", target = "leaveRequestId")

    @Mapping(source = "createdBy.id", target = "creatorId")
    @Mapping(source = "createdBy.fullName", target = "creatorFullName")
    LeaveDTO toDto(Leave leave);
}