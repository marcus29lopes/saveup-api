package com.saveup.api.wallet.controller;

import com.saveup.api.wallet.dto.WalletCreateRequest;
import com.saveup.api.wallet.dto.WalletResponse;
import com.saveup.api.wallet.dto.WalletUpdateRequest;
import com.saveup.api.wallet.entity.Wallet;
import com.saveup.api.wallet.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<WalletResponse> createWallet(@Valid @RequestBody WalletCreateRequest request, Authentication authentication) {
        String email = authentication.getName();

        return ResponseEntity.status(HttpStatus.CREATED).body(walletService.createWallet(request, email));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WalletResponse> getWallet(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();

        return ResponseEntity.status(HttpStatus.OK).body(walletService.findByIdAndUser(id, email));
    }

    @GetMapping
    public ResponseEntity<List<WalletResponse>> getAllWallets(
            Authentication authentication
    ) {
        String email = authentication.getName();

        return ResponseEntity.ok(
                walletService.findAll(email)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<WalletResponse> updateWallet(
            @PathVariable Long id,
            @Valid @RequestBody WalletUpdateRequest request,Authentication authentication){
        String email = authentication.getName();

        return ResponseEntity.status(HttpStatus.OK).body(walletService.updateWallet(id, request,email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWallet(@PathVariable Long id,Authentication authentication) {
        String email = authentication.getName();

        walletService.deleteWallet(id,email);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
