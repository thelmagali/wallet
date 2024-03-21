package com.playtomic.tests.wallet.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtomic.tests.wallet.api.WalletController;
import com.playtomic.tests.wallet.api.dto.DepositRequest;
import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.service.DepositService;
import com.playtomic.tests.wallet.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class WalletControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WalletRepository walletRepository;
    private Wallet wallet;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        walletRepository.deleteAll();
        wallet = new Wallet(new BigDecimal("100.00"));
        walletRepository.save(wallet);
    }

    @Test
    public void testGetWalletByUuid() throws Exception {
        mockMvc.perform(get("/api/wallets/{uuid}", wallet.getUuid()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(wallet.getUuid()))
                .andExpect(jsonPath("$.balance").value(100.00));
    }

    @Test
    public void testDepositAmount() throws Exception {
        mockMvc.perform(post("/api/wallets/{uuid}/deposits", wallet.getUuid())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new DepositRequest("1234567890123456", new BigDecimal("50.00")))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.depositUuid").isNotEmpty())
                .andExpect(jsonPath("$.amount").value(50.00));
    }
}
