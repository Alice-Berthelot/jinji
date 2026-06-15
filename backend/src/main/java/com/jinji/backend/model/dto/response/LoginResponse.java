package com.jinji.backend.model.dto.response;

public record LoginResponse(
        String accessToken,
        String refreshToken
) {}