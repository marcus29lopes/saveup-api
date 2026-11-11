package com.saveup.api.transaction.controller;

import com.saveup.api.transaction.dto.TransactionRequest;
import com.saveup.api.transaction.dto.TransactionResponse;
import com.saveup.api.transaction.entity.Transaction;
import com.saveup.api.transaction.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/{walletId}")
    public ResponseEntity<Void> createTransaction(@RequestParam Transaction.TransactionType type,
                                                  @Valid @RequestBody TransactionRequest transactionRequest,
                                                  @PathVariable Long walletId) {
        switch (type) {
            case DEPOSIT ->
                    transactionService.deposit(walletId, transactionRequest.amount(), transactionRequest.description());
            case WITHDRAWAL ->
                    transactionService.withdraw(walletId, transactionRequest.amount(), transactionRequest.description());
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @GetMapping("/wallet/{walletId}")
    public ResponseEntity<List<TransactionResponse>> getAllTransactions(
            @PathVariable Long walletId) {

        return ResponseEntity.status(HttpStatus.OK).body(transactionService.getAll(walletId));
    }
}
