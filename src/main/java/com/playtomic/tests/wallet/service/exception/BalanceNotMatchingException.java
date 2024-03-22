package com.playtomic.tests.wallet.service.exception;

import java.math.BigDecimal;
import lombok.NonNull;

public class BalanceNotMatchingException extends RuntimeException {
    private final String uuid;
    private final BigDecimal providedBalance;
    private final BigDecimal actualBalance;

    public BalanceNotMatchingException(@NonNull String uuid, @NonNull BigDecimal providedBalance, @NonNull BigDecimal actualBalance) {
        this.uuid = uuid;
        this.providedBalance = providedBalance;
        this.actualBalance = actualBalance;
    }

    @Override
    public String getMessage() {
        return String.format("Wallet balance does not match. UUID=[%s], Provided Balance=[%s], Actual Balance=[%s]", uuid, providedBalance.toPlainString(), actualBalance.toPlainString());
    }
}
