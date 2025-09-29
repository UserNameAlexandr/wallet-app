package com.wallet.wallet_app.exception;

import java.util.UUID;

public class WalletNotFoundException extends RuntimeException {
    public WalletNotFoundException(String message) {
        super(message);
    }
}
