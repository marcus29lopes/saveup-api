package com.saveup.api.transaction.dto;

import com.saveup.api.transaction.entity.Transaction;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionResponse(Long id, Transaction.TransactionType type, String description, BigDecimal amount, Instant createdAt) {

}
