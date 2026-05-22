package com.jinji.backend.mapper;

import com.jinji.backend.model.dto.LeaveRequestSummaryDTO;
import com.jinji.backend.model.dto.MyLeaveRequestSummaryDTO;
import com.jinji.backend.repository.projection.LeaveRequestSummaryRaw;
import com.jinji.backend.repository.projection.MyLeaveRequestSummaryRaw;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LeaveRequestSummaryMapper {

    MyLeaveRequestSummaryDTO toMySummaryDto(
            MyLeaveRequestSummaryRaw raw
    );

    LeaveRequestSummaryDTO toSummaryDto(
            LeaveRequestSummaryRaw raw
    );

    List<MyLeaveRequestSummaryDTO> toMySummaryDtos(
            List<MyLeaveRequestSummaryRaw> raws
    );

    List<LeaveRequestSummaryDTO> toSummaryDtos(
            List<LeaveRequestSummaryRaw> raws
    );
}