package com.saveup.api.transaction.service;

import com.saveup.api.shared.exception.ResourceNotFoundException;
import com.saveup.api.transaction.dto.TransactionResponse;
import com.saveup.api.transaction.entity.Transaction;
import com.saveup.api.transaction.repository.TransactionRepository;
import com.saveup.api.wallet.entity.Wallet;
import com.saveup.api.wallet.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(WalletRepository walletRepository, TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void deposit(Long walletId, BigDecimal amount, String description) {

        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Wallet not found: " + walletId));

        wallet.deposit(amount);

        Transaction transaction = new Transaction(
                wallet,
                Transaction.TransactionType.DEPOSIT,
                amount,
                description
        );

        transactionRepository.save(transaction);
    }

    @Transactional
    public void withdraw(Long walletId, BigDecimal amount, String description) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Wallet not found: " + walletId));
        wallet.withdraw(amount);
        Transaction transaction = new Transaction(
                wallet,
                Transaction.TransactionType.WITHDRAWAL,
                amount,
                description
        );
        transactionRepository.save(transaction);

    }

    public List<TransactionResponse> getAll(Long walletId) {
        if (!walletRepository.existsById(walletId)) {
            throw new ResourceNotFoundException("Wallet not found: " + walletId);
        }
        return transactionRepository.findAllTransactionsDesc(walletId).stream().map(
                this::toResponse
        ).toList();

    }

    private TransactionResponse toResponse(Transaction t) {
        return new TransactionResponse(t.getId(), t.getType(), t.getDescription(), t.getAmount(), t.getCreatedAt());
    }
}
