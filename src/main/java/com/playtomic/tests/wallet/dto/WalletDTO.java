package com.playtomic.tests.wallet.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class WalletDTO {
    private final UUID uuid;
    private final BigDecimal balance;

    public WalletDTO(UUID uuid, BigDecimal balance) {
        this.uuid = uuid;
        this.balance = balance;
    }

    public UUID getUuid() {
        return uuid;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
