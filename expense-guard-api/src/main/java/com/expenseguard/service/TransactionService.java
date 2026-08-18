package com.expenseguard.service;

import com.expenseguard.dto.PageResponse;
import com.expenseguard.dto.TransactionRequest;
import com.expenseguard.dto.TransactionResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface TransactionService {

    PageResponse<TransactionResponse> list(
            String email, LocalDate from, LocalDate to, Long categoryId, Pageable pageable);

    TransactionResponse create(String email, TransactionRequest request);

    TransactionResponse get(String email, Long id);

    TransactionResponse update(String email, Long id, TransactionRequest request);

    void delete(String email, Long id);
}
