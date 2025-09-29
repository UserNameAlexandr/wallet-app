package com.wallet.wallet_app.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.UUID;


public class WalletDTO {

    @NotNull
    private UUID walletId;

    @NotNull
    private OperationType operationType;

    @NotNull
    @PositiveOrZero(message = "The amount must be positive")
    private BigDecimal amount;

    public enum OperationType {
        DEPOSIT,
        WITHDRAW,
    }

    public UUID getWalletId() {
        return walletId;
    }

    public void setWalletId(UUID walletId) {
        this.walletId = walletId;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
