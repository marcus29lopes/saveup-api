package com.saveup.api.transaction.controller;

import com.saveup.api.transaction.dto.ReportCategoriesResponse;
import com.saveup.api.transaction.dto.TransactionRequest;
import com.saveup.api.transaction.dto.TransactionResponse;
import com.saveup.api.transaction.entity.Transaction;
import com.saveup.api.transaction.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
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
                                                  @PathVariable Long walletId,
                                                  Authentication authentication) {
        String email = authentication.getName();
        switch (type) {
            case DEPOSIT ->
                    transactionService.deposit(walletId, transactionRequest.amount(), transactionRequest.description(), email, transactionRequest.categoryId());
            case WITHDRAWAL ->
                    transactionService.withdraw(walletId, transactionRequest.amount(), transactionRequest.description(), email, transactionRequest.categoryId());
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @GetMapping("/wallet/{walletId}")
    public ResponseEntity<Page<TransactionResponse>> getAllTransactions(
            @PathVariable Long walletId,
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        String email = authentication.getName();

        return ResponseEntity.ok(
                transactionService.getAll(walletId, email, page, size)
        );
    }

    @GetMapping("/report/categories/{walletId}")
    public ResponseEntity<List<ReportCategoriesResponse>> getReportCategories(
            @PathVariable Long walletId,
            Authentication authentication,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dtStart,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dtEnd,
            @RequestParam Transaction.TransactionType type
    ) {
        String email = authentication.getName();

        return ResponseEntity.ok(
                transactionService.getReportCategories(
                        walletId,
                        email,
                        dtStart,
                        dtEnd,
                        type
                )
        );
    }
}
