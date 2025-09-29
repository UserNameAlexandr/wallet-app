package com.wallet.wallet_app.service;

import com.wallet.wallet_app.dto.WalletDTO;
import com.wallet.wallet_app.dto.WalletResponse;
import com.wallet.wallet_app.entity.Wallet;
import com.wallet.wallet_app.exception.InsufficientFundsException;
import com.wallet.wallet_app.exception.WalletNotFoundException;
import com.wallet.wallet_app.repository.WalletRepository;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Transactional
    @Retryable(value = {Exception.class}, maxAttempts = 5, backoff = @Backoff(delay = 200))
    public WalletResponse BalanceOperation (WalletDTO walletDTO) {
        Wallet wallet = walletRepository.findByIdWithLock(walletDTO.getWalletId())
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found"));

        if (walletDTO.getOperationType() == WalletDTO.OperationType.DEPOSIT) {
            wallet.setBalance(wallet.getBalance().add(walletDTO.getAmount()));
        } else if (walletDTO.getOperationType()== WalletDTO.OperationType.WITHDRAW) {
            if (wallet.getBalance().compareTo(walletDTO.getAmount()) < 0) {
                throw new InsufficientFundsException("insufficient funds in the wallet");
            }
            wallet.setBalance(wallet.getBalance().subtract(walletDTO.getAmount()));
        } else {
            throw new IllegalArgumentException("invalid operation type");
        }

        walletRepository.save(wallet);
        return new WalletResponse(wallet.getId(), wallet.getBalance());
    }


    public BigDecimal getBalance(UUID walletId) {
        Wallet walletBalance = walletRepository.findById(walletId)
                .orElseThrow(()->new WalletNotFoundException("Wallet not found"));
        return walletBalance.getBalance();
    }

}
