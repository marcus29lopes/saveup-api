package com.saveup.api.wallet.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record WalletResponse(Long id, String name, BigDecimal balance, Instant createdAt) {
}
