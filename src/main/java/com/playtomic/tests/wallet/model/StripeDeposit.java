package com.playtomic.tests.wallet.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;

@Entity
@Table(name = "stripe_deposit")
public class StripeDeposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "deposit_id", referencedColumnName = "id", nullable = false)
    private Deposit deposit;

    @Column(name = "stripe_payment_id", nullable = false)
    private String stripePaymentId;

    @Column(name = "credit_card_number", nullable = false)
    private String creditCardNumber;

    public StripeDeposit(Deposit deposit, String stripePaymentId, String creditCardNumber) {
        this.deposit = deposit;
        this.stripePaymentId = stripePaymentId;
        this.creditCardNumber = creditCardNumber;
    }

    public StripeDeposit() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Deposit getDeposit() {
        return deposit;
    }

    public void setDeposit(Deposit deposit) {
        this.deposit = deposit;
    }

    public String getStripePaymentId() {
        return stripePaymentId;
    }

    public void setStripePaymentId(String stripePaymentId) {
        this.stripePaymentId = stripePaymentId;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }
}
