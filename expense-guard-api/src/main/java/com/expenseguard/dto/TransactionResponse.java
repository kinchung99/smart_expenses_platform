package com.expenseguard.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponse(
        Long id,
        String categoryName,
        BigDecimal amount,
        String description,
        LocalDate transactionDate,
        Double anomalyScore,
        boolean flagged) {
}
