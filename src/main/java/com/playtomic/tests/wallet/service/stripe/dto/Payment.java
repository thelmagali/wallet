package com.playtomic.tests.wallet.service.stripe.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.NonNull;

public class Payment {

    @NonNull
    private String id;

    @JsonCreator
    public Payment(@JsonProperty(value = "id", required = true) String id, BigDecimal amount) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
