package com.jinji.backend.mapper;

import com.jinji.backend.model.dto.response.HrPolicyDTO;
import com.jinji.backend.model.entity.HrPolicy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HrPolicyMapper {

    HrPolicyDTO toDto(HrPolicy entity);
}