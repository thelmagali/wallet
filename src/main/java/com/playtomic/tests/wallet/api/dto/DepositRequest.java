package com.playtomic.tests.wallet.api.dto;

import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.CreditCardNumber;

public class DepositRequest {
    @NotBlank(message = "Credit card number cannot be blank")
    @CreditCardNumber(message = "Invalid credit card number")
    private final String creditCardNumber;
    private final BigDecimal amount;
    private final BigDecimal currentBalance;

    public DepositRequest(String creditCardNumber, BigDecimal amount, BigDecimal currentBalance) {
        this.creditCardNumber = creditCardNumber;
        this.amount = amount;
        this.currentBalance = currentBalance;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DepositRequest)) return false;
        DepositRequest that = (DepositRequest) o;
        return Objects.equals(getCreditCardNumber(), that.getCreditCardNumber()) && Objects.equals(getAmount(), that.getAmount()) && Objects.equals(getCurrentBalance(), that.getCurrentBalance());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCreditCardNumber(), getAmount(), getCurrentBalance());
    }
}
