package com.expenseguard.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequest(
        @NotNull Long categoryId,
        @NotNull @Positive BigDecimal amount,
        String description,
        @NotNull LocalDate transactionDate) {
}
