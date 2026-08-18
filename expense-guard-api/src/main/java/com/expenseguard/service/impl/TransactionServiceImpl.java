package com.expenseguard.service.impl;

import com.expenseguard.dto.PageResponse;
import com.expenseguard.dto.TransactionRequest;
import com.expenseguard.dto.TransactionResponse;
import com.expenseguard.entity.Category;
import com.expenseguard.entity.Transaction;
import com.expenseguard.entity.User;
import com.expenseguard.exception.ResourceNotFoundException;
import com.expenseguard.repository.CategoryRepository;
import com.expenseguard.repository.TransactionRepository;
import com.expenseguard.repository.TransactionSpecifications;
import com.expenseguard.repository.UserRepository;
import com.expenseguard.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<TransactionResponse> list(
            String email, LocalDate from, LocalDate to, Long categoryId, Pageable pageable) {

        User user = currentUser(email);

        Specification<Transaction> spec = TransactionSpecifications.ownedBy(user.getId());
        if (categoryId != null) {
            spec = spec.and(TransactionSpecifications.hasCategory(categoryId));
        }
        if (from != null) {
            spec = spec.and(TransactionSpecifications.dateFrom(from));
        }
        if (to != null) {
            spec = spec.and(TransactionSpecifications.dateTo(to));
        }

        return PageResponse.from(
                transactionRepository.findAll(spec, pageable), TransactionServiceImpl::toResponse);
    }

    @Override
    @Transactional
    public TransactionResponse create(String email, TransactionRequest request) {
        User user = currentUser(email);
        Category category = findCategory(request.categoryId());

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setCategory(category);
        applyRequest(transaction, request);

        return toResponse(transactionRepository.save(transaction));
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionResponse get(String email, Long id) {
        return toResponse(findOwned(email, id));
    }

    @Override
    @Transactional
    public TransactionResponse update(String email, Long id, TransactionRequest request) {
        Transaction transaction = findOwned(email, id);
        transaction.setCategory(findCategory(request.categoryId()));
        applyRequest(transaction, request);

        return toResponse(transaction);
    }

    @Override
    @Transactional
    public void delete(String email, Long id) {
        transactionRepository.delete(findOwned(email, id));
    }

    private void applyRequest(Transaction transaction, TransactionRequest request) {
        transaction.setAmount(request.amount());
        transaction.setDescription(request.description());
        transaction.setTransactionDate(request.transactionDate());
    }

    /**
     * Returns 404 rather than 403 for another user's transaction so ids cannot be probed.
     */
    private Transaction findOwned(String email, Long id) {
        return transactionRepository.findByIdAndUserId(id, currentUser(email).getId())
                .orElseThrow(() -> ResourceNotFoundException.of("Transaction", id));
    }

    private Category findCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> ResourceNotFoundException.of("Category", categoryId));
    }

    private User currentUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user with email " + email));
    }

    private static TransactionResponse toResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getCategory().getName(),
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getTransactionDate(),
                transaction.getAnomalyScore(),
                transaction.isFlagged());
    }
}
