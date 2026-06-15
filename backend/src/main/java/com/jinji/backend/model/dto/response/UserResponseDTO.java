package com.jinji.backend.model.dto.response;

import java.util.Set;

public record UserResponseDTO(
        Long id,
        String username,
        boolean isActive,
        Set<String> roles
) {}