package com.saveup.api.user.dto;

public record LoginResponse(
        String accessToken,
        String tokenType
) {
}