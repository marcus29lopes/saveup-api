package com.saveup.api.wallet.repository;

import com.saveup.api.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {


    Optional<Wallet> findByIdAndUser_Email(Long id, String email);

    List<Wallet> findAllByUser_Email(String email);
}
