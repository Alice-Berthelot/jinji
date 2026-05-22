package com.jinji.backend.mapper;

import com.jinji.backend.model.dto.LeaveRequestReviewDTO;
import com.jinji.backend.model.entity.LeaveRequestReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LeaveRequestReviewMapper {

    @Mapping(source = "reviewedBy.id", target = "reviewerId")
    @Mapping(source = "reviewedBy.firstName", target = "reviewerFirstName")
    @Mapping(source = "reviewedBy.surname", target = "reviewerLastName")
    LeaveRequestReviewDTO toDto(LeaveRequestReview review);

    List<LeaveRequestReviewDTO> toDtos(List<LeaveRequestReview> reviews);
}