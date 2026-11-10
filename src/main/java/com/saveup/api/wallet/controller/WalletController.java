package com.saveup.api.wallet.controller;

import com.saveup.api.wallet.dto.WalletCreateRequest;
import com.saveup.api.wallet.dto.WalletResponse;
import com.saveup.api.wallet.dto.WalletUpdateRequest;
import com.saveup.api.wallet.entity.Wallet;
import com.saveup.api.wallet.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/wallet")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<WalletResponse> createWallet(@Valid @RequestBody WalletCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(walletService.createWallet(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WalletResponse> getWallet(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(walletService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<WalletResponse>> getAllWallets() {
        return ResponseEntity.status(HttpStatus.OK).body(walletService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<WalletResponse> updateWallet(
            @PathVariable Long id,
            @Valid @RequestBody WalletUpdateRequest request) {

        return ResponseEntity.status(HttpStatus.OK).body(walletService.updateWallet(id, request));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWallet(@PathVariable Long id) {
        walletService.deleteWallet( id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
