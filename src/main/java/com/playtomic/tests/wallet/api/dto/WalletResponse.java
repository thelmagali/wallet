package com.playtomic.tests.wallet.api.dto;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class WalletResponse {
    private final UUID uuid;
    private final BigDecimal balance;

    public WalletResponse(UUID uuid, BigDecimal balance) {
        this.uuid = uuid;
        this.balance = balance;
    }

    public UUID getUuid() {
        return uuid;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WalletResponse)) return false;
        WalletResponse walletResponse = (WalletResponse) o;
        return Objects.equals(getUuid(), walletResponse.getUuid()) && Objects.equals(getBalance(), walletResponse.getBalance());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid(), getBalance());
    }
}
