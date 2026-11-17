package com.saveup.api.wallet.entity;

import com.saveup.api.shared.exception.InsufficientBalanceException;
import com.saveup.api.transaction.entity.Transaction;
import com.saveup.api.user.entity.ApiUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Table(name = "wallet")
@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private ApiUser user;

    protected Wallet() {

    }

    public Wallet(String name, ApiUser user) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Wallet name cannot be empty"
            );
        }

        if (name.length() > 100) {
            throw new IllegalArgumentException(
                    "Wallet name cannot exceed 100 characters"
            );
        }

        if (user == null) {
            throw new IllegalArgumentException(
                    "Wallet owner cannot be null"
            );
        }

        this.name = name;
        this.user = user;
        this.balance = BigDecimal.ZERO;
        this.createdAt = Instant.now();
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Long getId() {
        return id;
    }

    public void rename(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Wallet name cannot be empty");
        }
        if (newName.length() > 100) {
            throw new IllegalArgumentException("Wallet name cannot exceed 100 characters");
        }
        this.name = newName;
    }
    public void deposit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        this.balance = this.balance.add(amount);
    }

    public ApiUser getUser() {
        return user;
    }

    public void withdraw(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        if (balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        this.balance = this.balance.subtract(amount);
    }
}
