package com.jinji.backend.mapper;

import com.jinji.backend.model.dto.response.LeaveRequestActionResponseDTO;
import com.jinji.backend.model.dto.LeaveRequestDTO;
import com.jinji.backend.model.entity.LeaveRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LeaveRequestMapper {

    @Mapping(source = "leaveType.label", target = "leaveTypeLabel")
    LeaveRequestDTO toDto(LeaveRequest leaveRequest);

    List<LeaveRequestDTO> toDtos(List<LeaveRequest> leaveRequests);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "status", source = "status")
    LeaveRequestActionResponseDTO toActionResponseDto(LeaveRequest leaveRequest);
}