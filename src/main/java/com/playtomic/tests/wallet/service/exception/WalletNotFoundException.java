package com.playtomic.tests.wallet.service.exception;

import lombok.NonNull;

public class WalletNotFoundException extends RuntimeException {
    private final String uuid;

    public WalletNotFoundException(@NonNull String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String getMessage() {
        return String.format("Wallet not found. UUID=[%s]", uuid);
    }
}
