package com.jinji.backend.mapper;

import com.jinji.backend.model.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    default String toCode(Role role) {
        return role.getCode().name();
    }
}