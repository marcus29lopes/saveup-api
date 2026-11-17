package com.saveup.api.transaction.service;

import com.saveup.api.category.entity.Categories;
import com.saveup.api.category.repository.CategoriesRepository;
import com.saveup.api.shared.exception.ResourceNotFoundException;
import com.saveup.api.transaction.dto.TransactionResponse;
import com.saveup.api.transaction.entity.Transaction;
import com.saveup.api.transaction.repository.TransactionRepository;
import com.saveup.api.wallet.entity.Wallet;
import com.saveup.api.wallet.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortArgumentResolver;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final CategoriesRepository categoriesRepository;

    public TransactionService(WalletRepository walletRepository, TransactionRepository transactionRepository, CategoriesRepository categoriesRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.categoriesRepository = categoriesRepository;
    }

    @Transactional
    public void deposit(Long walletId, BigDecimal amount, String description, String email, Long categoryId) {

        Wallet wallet = walletRepository.findByIdAndUser_Email(walletId, email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Wallet not found: " + walletId));
        Categories category = categoriesRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found: " + categoryId));


        wallet.deposit(amount);

        Transaction transaction = new Transaction(
                wallet,
                Transaction.TransactionType.DEPOSIT,
                amount,
                description, category
        );

        transactionRepository.save(transaction);
    }

    @Transactional
    public void withdraw(Long walletId, BigDecimal amount, String description, String email, Long categoryId) {
        Wallet wallet = walletRepository.findByIdAndUser_Email(walletId, email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Wallet not        found: " + walletId));
        Categories category = categoriesRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found: " + categoryId));

        wallet.withdraw(amount);
        Transaction transaction = new Transaction(
                wallet,
                Transaction.TransactionType.WITHDRAWAL,
                amount,
                description,
                category
        );
        transactionRepository.save(transaction);

    }

    public Page<TransactionResponse> getAll(Long walletId, String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")
        );

        walletRepository.findByIdAndUser_Email(walletId, email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Wallet not found: " + walletId
                        )
                );

        return transactionRepository.findAllTransactionsDesc(walletId, pageable).map(
                this::toResponse
        );

    }

    private TransactionResponse toResponse(Transaction t) {
        return new TransactionResponse(t.getId(), t.getType(), t.getDescription(), t.getAmount(), t.getCreatedAt(),t.getCategories().getName());
    }
}



