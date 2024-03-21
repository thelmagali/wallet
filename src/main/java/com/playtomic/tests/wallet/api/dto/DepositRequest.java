package com.playtomic.tests.wallet.api.dto;

import java.math.BigDecimal;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DepositRequest)) return false;
        DepositRequest that = (DepositRequest) o;
        return Objects.equals(getCreditCardNumber(), that.getCreditCardNumber()) && Objects.equals(getAmount(), that.getAmount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCreditCardNumber(), getAmount());
    }
}
