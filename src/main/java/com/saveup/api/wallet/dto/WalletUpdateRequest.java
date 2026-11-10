package com.saveup.api.wallet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record WalletUpdateRequest(
        @NotBlank(message = "Name cannot be blank")
        @Size(max = 100, message = "Wallet name cannot exceed 100 characters")
        String name) {
}
