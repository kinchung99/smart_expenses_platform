package com.expenseguard.repository;

import com.expenseguard.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    /**
     * Scoped by user so one account can never read another's transaction by guessing an id.
     */
    Optional<Transaction> findByIdAndUserId(Long id, Long userId);
}
