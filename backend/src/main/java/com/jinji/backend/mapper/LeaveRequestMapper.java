package com.jinji.backend.mapper;

import com.jinji.backend.model.dto.response.LeaveRequestActionResponseDTO;
import com.jinji.backend.model.dto.response.LeaveRequestDTO;
import com.jinji.backend.model.dto.response.LeaveRequestSummaryDTO;
import com.jinji.backend.model.dto.response.MyLeaveRequestSummaryDTO;
import com.jinji.backend.model.entity.LeaveRequest;
import com.jinji.backend.model.enums.LeaveRequestWorkflowStatus;
import com.jinji.backend.model.projection.LeaveRequestSummary;
import com.jinji.backend.model.projection.MyLeaveRequestSummary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LeaveRequestMapper {

    @Mapping(source = "id", target = "leaveRequestId")
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.firstName", target = "employeeFirstName")
    @Mapping(source = "employee.surname", target = "employeeSurname")
    @Mapping(source = "leaveType.label", target = "leaveTypeLabel")
    LeaveRequestDTO toDto(LeaveRequest leaveRequest);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "status", source = "status")
    LeaveRequestActionResponseDTO toActionResponseDto(LeaveRequest leaveRequest);

    MyLeaveRequestSummaryDTO toMySummaryDto(MyLeaveRequestSummary raw);

    LeaveRequestSummaryDTO toSummaryDto(
            LeaveRequestSummary source,
            LeaveRequestWorkflowStatus workflowStatus,
            String statusLabel
    );
}