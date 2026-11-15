package com.saveup.api.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterResponse(
        @NotBlank
        Long id,
        @NotBlank
        String name,
        @Email
        String email
) {
}
