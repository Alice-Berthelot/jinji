package com.jinji.backend.model.dto.response;

import java.time.LocalDateTime;

public record NotificationDTO(
        Long id,
        LocalDateTime createdAt,
        boolean read,
        String message,
        Long userId
) {}