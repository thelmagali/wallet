package com.playtomic.tests.wallet.model;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "wallet")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false, nullable = false, unique = true)
    private String uuid;

    @Column(precision = 19, scale = 2)
    private BigDecimal balance;

    public Wallet() {
    }

    public Wallet(BigDecimal balance) {
        this.balance = balance;
        this.uuid = UUID.randomUUID().toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Wallet)) return false;
        Wallet wallet = (Wallet) o;
        return Objects.equals(getId(), wallet.getId()) && Objects.equals(getUuid(), wallet.getUuid()) && Objects.equals(getBalance(), wallet.getBalance());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUuid(), getBalance());
    }
}
