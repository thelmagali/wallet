package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.api.dto.DepositRequest;
import com.playtomic.tests.wallet.api.dto.DepositResponse;
import com.playtomic.tests.wallet.mapper.DepositConverter;
import com.playtomic.tests.wallet.model.Deposit;
import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.service.stripe.StripeService;
import com.playtomic.tests.wallet.service.stripe.dto.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepositServiceTest {
    @Mock
    private StripeService stripeService;
    @Mock
    private DepositWriter depositWriter;
    @Mock
    private WalletService walletService;
    @Mock
    private DepositConverter depositConverter;
    @InjectMocks
    private DepositService depositService;
    private final Wallet wallet = new Wallet(new BigDecimal("100.00"));
    private final DepositRequest depositRequest = new DepositRequest("1234567890123456", new BigDecimal("50.00"));
    private final Payment stripePayment = new Payment("stripePaymentId", depositRequest.getAmount());
    private final Deposit deposit = mock(Deposit.class);
    private final DepositResponse depositResponse = new DepositResponse(UUID.randomUUID(), depositRequest.getAmount());

    @BeforeEach
    void setUp() {
        when(walletService.getWallet(anyString())).thenReturn(wallet);
        when(stripeService.charge(anyString(), any(BigDecimal.class))).thenReturn(stripePayment);
    }

    @Test
    void deposit_testHappyPath() {
        when(depositWriter.saveDeposit(wallet, depositRequest, stripePayment)).thenReturn(deposit);
        when(depositConverter.entityToResponse(deposit)).thenReturn(depositResponse);

        DepositResponse response = depositService.deposit(wallet.getUuid(), depositRequest);

        verify(walletService).topUpBalance(depositRequest, wallet);
        verify(depositWriter).saveDeposit(wallet, depositRequest, stripePayment);
        assertThat(response.getDepositUuid()).isEqualTo(depositResponse.getDepositUuid());
        assertThat(response.getAmount()).isEqualTo(depositResponse.getAmount());
    }

    @Test
    void deposit_refundCalled_whenTopUpBalanceFails() {
        doThrow(new RuntimeException("Top up failed")).when(walletService).topUpBalance(depositRequest, wallet);
        assertThrows(RuntimeException.class, () -> depositService.deposit(wallet.getUuid(), depositRequest));

        verify(stripeService).refund(stripePayment.getId());
        verifyNoInteractions(depositWriter);
    }

    @Test
    void deposit_refundCalled_whenSaveDepositFails() {
        doThrow(new RuntimeException("Save deposit failed")).when(depositWriter).saveDeposit(wallet, depositRequest, stripePayment);
        assertThrows(RuntimeException.class, () -> depositService.deposit(wallet.getUuid(), depositRequest));

        verify(walletService).topUpBalance(depositRequest, wallet);
        verify(stripeService).refund(stripePayment.getId());
    }
}
