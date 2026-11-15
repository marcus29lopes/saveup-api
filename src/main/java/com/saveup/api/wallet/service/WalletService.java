package com.saveup.api.wallet.service;

import com.saveup.api.shared.exception.ResourceNotFoundException;
import com.saveup.api.user.entity.ApiUser;
import com.saveup.api.user.repository.ApiUserRepository;
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
    private final ApiUserRepository apiUserRepository;

    public WalletService(WalletRepository walletRepository, ApiUserRepository apiUserRepository) {
        this.walletRepository = walletRepository;
        this.apiUserRepository = apiUserRepository;
    }

    public WalletResponse createWallet(WalletCreateRequest request, String email) {
        ApiUser user = apiUserRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));

        Wallet wallet = new Wallet(request.name(), user);
        Wallet savedWallet = walletRepository.save(wallet);

        return toResponse(savedWallet);

    }

    public WalletResponse findByIdAndUser(Long id, String email) {
        Wallet wallet = walletRepository.findByIdAndUser_Email(id, email).orElseThrow(() -> new ResourceNotFoundException("Wallet not found: " + id));
        return toResponse(wallet);
    }

    public List<WalletResponse> findAll(String email) {

        List<Wallet> wallets =
                walletRepository.findAllByUser_Email(email);

        return wallets.stream()
                .map(this::toResponse)
                .toList();
    }

    private WalletResponse toResponse(Wallet wallet) {
        return new WalletResponse(wallet.getId(), wallet.getName(), wallet.getBalance(), wallet.getCreatedAt());
    }

    public WalletResponse updateWallet(Long id, WalletUpdateRequest request, String email) {
        Wallet wallet = walletRepository.findByIdAndUser_Email(id,email).orElseThrow(() -> new ResourceNotFoundException("Wallet not found: " + id));
        wallet.rename(request.name());
        Wallet savedWallet = walletRepository.save(wallet);
        return toResponse(savedWallet);
    }

    public void deleteWallet(Long id,String email) {
        Wallet wallet = walletRepository.findByIdAndUser_Email(id, email).orElseThrow(() -> new ResourceNotFoundException("Wallet not found: " + id));
        walletRepository.delete(wallet);
    }
}
