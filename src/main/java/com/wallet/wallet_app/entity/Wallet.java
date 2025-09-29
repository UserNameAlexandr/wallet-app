package com.wallet.wallet_app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "wallets")
public class Wallet {

    public Wallet(UUID id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }

    public Wallet() {}

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "balance", precision = 19, scale = 2)
    private BigDecimal balance;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
