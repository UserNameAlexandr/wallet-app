package com.wallet.wallet_app.controller;

import com.wallet.wallet_app.dto.WalletDTO;
import com.wallet.wallet_app.dto.WalletResponse;
import com.wallet.wallet_app.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping(path = "/wallet", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WalletResponse> changeWallet(@Valid @RequestBody WalletDTO walletDTO) {
        WalletResponse walletResponse = walletService.BalanceOperation(walletDTO);
        return ResponseEntity.ok(walletResponse);
    }

    @GetMapping("/wallet/{walletId}")
    public ResponseEntity<Map<String, BigDecimal>> getBalance (@PathVariable("walletId") UUID walletId) {
        BigDecimal balance = walletService.getBalance(walletId);
        Map<String,BigDecimal> response = new HashMap<>();
        response.put("balance",balance);
        return  ResponseEntity.ok(response);
    }
}
