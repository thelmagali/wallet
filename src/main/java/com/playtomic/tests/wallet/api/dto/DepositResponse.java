package com.playtomic.tests.wallet.api.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class DepositResponse {
    private final UUID depositUuid;
    private final BigDecimal amount;

    public DepositResponse(UUID depositUuid, BigDecimal amount) {
        this.depositUuid = depositUuid;
        this.amount = amount;
    }

    public UUID getDepositUuid() {
        return depositUuid;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
