package com.playtomic.tests.wallet.service.stripe;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.playtomic.tests.wallet.api.exception.GlobalExceptionHandler;
import com.playtomic.tests.wallet.service.stripe.dto.Payment;
import com.playtomic.tests.wallet.service.stripe.exception.StripeRestTemplateResponseErrorHandler;
import com.playtomic.tests.wallet.service.stripe.exception.StripeServiceException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;


/**
 * Handles the communication with Stripe.
 *
 * A real implementation would call to String using their API/SDK.
 * This dummy implementation throws an error when trying to charge less than 10€.
 */
@Service
public class StripeService {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @NonNull
    private URI chargesUri;

    @NonNull
    private URI refundsUri;

    @NonNull
    private RestTemplate restTemplate;

    public StripeService(@Value("${stripe.simulator.charges-uri}") @NonNull URI chargesUri,
                         @Value("${stripe.simulator.refunds-uri}") @NonNull URI refundsUri,
                         @NonNull RestTemplateBuilder restTemplateBuilder) {
        this.chargesUri = chargesUri;
        this.refundsUri = refundsUri;
        this.restTemplate =
                restTemplateBuilder
                .errorHandler(new StripeRestTemplateResponseErrorHandler())
                .build();
    }

    /**
     * Charges money in the credit card.
     *
     * Ignore the fact that no CVC or expiration date are provided.
     *
     * @param creditCardNumber The number of the credit card
     * @param amount The amount that will be charged.
     *
     * @throws StripeServiceException
     */
    public Payment charge(@NonNull String creditCardNumber, @NonNull BigDecimal amount) throws StripeServiceException {
        ChargeRequest body = new ChargeRequest(creditCardNumber, amount);
        final var payment = restTemplate.postForObject(chargesUri, body, Payment.class);
        assert payment != null;
        logger.info("Charged the stripe payment {}", payment.getId());
        return payment;
    }

    /**
     * Refunds the specified payment.
     */
    public void refund(@NonNull String paymentId) throws StripeServiceException {
        //  Object.class because we don't read the body here.
        restTemplate.postForEntity(chargesUri.toString(), null, Object.class, paymentId);
        logger.info("Refunding the payment {}", paymentId);
    }

    @AllArgsConstructor
    private static class ChargeRequest {

        @NonNull
        @JsonProperty("credit_card")
        String creditCardNumber;

        @NonNull
        @JsonProperty("amount")
        BigDecimal amount;
    }
}
