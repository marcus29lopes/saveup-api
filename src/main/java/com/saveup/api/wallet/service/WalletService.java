package com.saveup.api.wallet.service;

import com.saveup.api.shared.exception.ResourceNotFoundException;
import com.saveup.api.wallet.dto.WalletCreateRequest;
import com.saveup.api.wallet.dto.WalletResponse;
import com.saveup.api.wallet.dto.WalletUpdateRequest;
import com.saveup.api.wallet.entity.Wallet;
import com.saveup.api.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public WalletResponse createWallet(WalletCreateRequest request) {

        Wallet wallet = new Wallet(request.name());
        Wallet savedWallet = walletRepository.save(wallet);

        return toResponse(savedWallet);

    }

    public WalletResponse findById(Long id) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Wallet not found: " + id));
        return toResponse(wallet);
    }

    public List<WalletResponse> findAll() {
        List<Wallet> wallets = walletRepository.findAll();
        return wallets.stream().map(this::toResponse).toList();
    }

    public WalletResponse toResponse(Wallet wallet){
        return new WalletResponse(wallet.getId(), wallet.getName(), wallet.getBalance(), wallet.getCreatedAt());
    }
    public WalletResponse updateWallet(Long id, WalletUpdateRequest request) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Wallet not found: " + id));
        wallet.rename(request.name());
        Wallet savedWallet = walletRepository.save(wallet);
        return toResponse(savedWallet);
    }

    public void deleteWallet(Long id) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Wallet not found: " + id));
        walletRepository.delete(wallet);
    }
}
