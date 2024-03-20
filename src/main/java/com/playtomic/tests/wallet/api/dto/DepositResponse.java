package com.playtomic.tests.wallet.api.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class DepositResponse {
    private final UUID uuid;
    private final UUID walletUuid;
    private final BigDecimal amount;

    public DepositResponse(UUID uuid, UUID walletUuid, BigDecimal amount) {
        this.uuid = uuid;
        this.walletUuid = walletUuid;
        this.amount = amount;
    }

    public UUID getUuid() {
        return uuid;
    }

    public UUID getWalletUuid() {
        return walletUuid;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
