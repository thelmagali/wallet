package com.playtomic.tests.wallet.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtomic.tests.wallet.api.dto.DepositRequest;
import com.playtomic.tests.wallet.api.dto.DepositResponse;
import com.playtomic.tests.wallet.api.dto.WalletResponse;
import com.playtomic.tests.wallet.service.DepositService;
import com.playtomic.tests.wallet.service.WalletService;
import com.playtomic.tests.wallet.service.exception.WalletNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@WebMvcTest(WalletController.class)
public class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    @MockBean
    private DepositService depositService;

    @Autowired
    private ObjectMapper objectMapper;
    private final UUID walletUuid = UUID.randomUUID();
    private final WalletResponse walletResponse = new WalletResponse(walletUuid, new BigDecimal("100.00"));
    private final DepositRequest depositRequest = new DepositRequest("36070500001020", new BigDecimal("50.00"), new BigDecimal("100.00"));

    @Test
    public void getWalletByUuid_shouldReturnWallet_whenWalletExists() throws Exception {
        when(walletService.getWalletDTO(walletUuid.toString())).thenReturn(walletResponse);

        mockMvc.perform(get("/api/wallets/" + walletUuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(walletUuid.toString()))
                .andExpect(jsonPath("$.balance").value(100.00));
    }

    @Test
    public void getWalletByUuid_shouldReturnNotFound_whenWalletDoesNotExist() throws Exception {
        when(walletService.getWalletDTO(walletUuid.toString())).thenThrow(WalletNotFoundException.class);

        mockMvc.perform(get("/api/wallets/" + walletUuid))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getWalletByUuid_shouldReturnInternalServerError_forAnyOtherException() throws Exception {
        when(walletService.getWalletDTO(walletUuid.toString())).thenThrow(RuntimeException.class);

        mockMvc.perform(get("/api/wallets/" + walletUuid))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void depositAmount_shouldReturnDepositData_whenDataIsCorrect() throws Exception {
        final var depositResponse = new DepositResponse(UUID.randomUUID(), new BigDecimal("50.00"));
        when(depositService.deposit(walletUuid.toString(), depositRequest)).thenReturn(depositResponse);

        mockMvc.perform(post("/api/wallets/" + walletUuid + "/deposits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.depositUuid").exists())
                .andExpect(jsonPath("$.amount").value(50.00));
    }

    @Test
    public void depositAmount_shouldReturnNotFound_whenWalletNotFound() throws Exception {
        when(depositService.deposit(walletUuid.toString(), depositRequest)).thenThrow(WalletNotFoundException.class);

        mockMvc.perform(post("/api/wallets/" + walletUuid + "/deposits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void depositAmount_shouldReturnInternalServerError_forAnyException() throws Exception {
        when(depositService.deposit(walletUuid.toString(), depositRequest)).thenThrow(RuntimeException.class);

        mockMvc.perform(post("/api/wallets/" + walletUuid + "/deposits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(status().isInternalServerError());
    }
}
