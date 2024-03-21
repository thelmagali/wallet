package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.api.dto.DepositRequest;
import com.playtomic.tests.wallet.api.dto.DepositResponse;
import com.playtomic.tests.wallet.api.dto.WalletDTO;
import com.playtomic.tests.wallet.service.DepositService;
import com.playtomic.tests.wallet.service.WalletService;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WalletControllerTest {

    @Mock
    private WalletService walletService;

    @Mock
    private DepositService depositService;

    @InjectMocks
    private WalletController walletController;

    @Test
    void testGetWalletByUuid() {
        final var walletUuid = UUID.randomUUID();
        final var mockWalletDTO = new WalletDTO(walletUuid, new BigDecimal("100.00"));
        when(walletService.getWalletDTO(any(String.class))).thenReturn(mockWalletDTO);

        ResponseEntity<WalletDTO> response = walletController.getWalletByUuid(walletUuid.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(walletUuid, Objects.requireNonNull(response.getBody()).getUuid());
        assertEquals(new BigDecimal("100.00"), response.getBody().getBalance());
    }

    @Test
    void testDepositAmount() {
        final var depositUuid = UUID.randomUUID();
        final var depositAmount = new BigDecimal("50.00");
        final var depositRequest = new DepositRequest("1234567890123456", depositAmount);
        final var depositResponse = new DepositResponse(depositUuid, depositAmount);
        final var walletUUID = "walletUUID";
        when(depositService.deposit(walletUUID, depositRequest)).thenReturn(depositResponse);

        ResponseEntity<DepositResponse> response = walletController.depositAmount(walletUUID, depositRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(depositUuid, Objects.requireNonNull(response.getBody()).getDepositUuid());
        assertEquals(depositAmount, response.getBody().getAmount());
    }
}
