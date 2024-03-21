package com.playtomic.tests.wallet.service;


import com.playtomic.tests.wallet.service.stripe.dto.Payment;
import com.playtomic.tests.wallet.service.stripe.exception.StripeAmountTooSmallException;
import com.playtomic.tests.wallet.service.stripe.exception.StripeServiceException;
import com.playtomic.tests.wallet.service.stripe.StripeService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.net.URI;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class StripeServiceTest {
    @Mock
    private RestTemplateBuilder restTemplateBuilder;
    @Mock
    private RestTemplate restTemplate;
    private StripeService stripeService;

    @BeforeEach
    void setUp() {
        URI testUri = URI.create("http://how-would-you-test-me.localhost");
        when(restTemplateBuilder.errorHandler(any())).thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        stripeService = new StripeService(testUri, testUri, restTemplateBuilder);
    }

    @Test
    public void test_exception() {
        when(restTemplate.postForObject(any(), any(), any()))
                .thenThrow(new StripeAmountTooSmallException());

        Assertions.assertThrows(StripeAmountTooSmallException.class, () -> {
            stripeService.charge("4242 4242 4242 4242", new BigDecimal(5));
        });
    }

    @Test
    public void test_ok() throws StripeServiceException {
        Payment payment = new Payment("paymentId", new BigDecimal("15"));
        when(restTemplate.postForObject(any(), any(), any()))
                .thenReturn(payment);

        stripeService.charge("4242 4242 4242 4242", new BigDecimal(15));
    }
}
