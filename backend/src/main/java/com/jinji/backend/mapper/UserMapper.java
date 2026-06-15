package com.jinji.backend.mapper;

import com.jinji.backend.model.dto.response.UserResponseDTO;
import com.jinji.backend.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface UserMapper {

    @Mapping(target = "roles", expression = "java(mapRoles(user))")
    UserResponseDTO toDto(User user);

    default Set<String> mapRoles(User user) {
        return user.getRoles()
                .stream()
                .map(role -> role.getCode().name())
                .collect(Collectors.toSet());
    }
}