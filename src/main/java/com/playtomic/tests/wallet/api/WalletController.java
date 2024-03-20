package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.dto.WalletDTO;
import com.playtomic.tests.wallet.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {
    private final WalletService walletService;
    public WalletController(final WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<WalletDTO> getWalletByUuid(@PathVariable String uuid) {
        WalletDTO walletDTO = walletService.getWallet(uuid);
        return ResponseEntity.ok(walletDTO);
    }
}
