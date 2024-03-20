package com.playtomic.tests.wallet.api.dto;

import java.math.BigDecimal;

public class DepositRequest {
    private final String creditCardNumber;
    private final BigDecimal amount;

    public DepositRequest(String creditCardNumber, BigDecimal amount) {
        this.creditCardNumber = creditCardNumber;
        this.amount = amount;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
