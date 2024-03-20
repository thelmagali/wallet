package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.api.dto.DepositRequest;
import com.playtomic.tests.wallet.api.dto.DepositResponse;
import com.playtomic.tests.wallet.api.dto.WalletDTO;
import com.playtomic.tests.wallet.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {
    private final WalletService walletService;
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<WalletDTO> getWalletByUuid(@PathVariable String uuid) {
        WalletDTO walletDTO = walletService.getWallet(uuid);
        return ResponseEntity.ok(walletDTO);
    }

    @PostMapping("/{uuid}/deposits")
    public ResponseEntity<DepositResponse> depositAmount(@PathVariable String uuid, @RequestBody DepositRequest depositRequest) {
        DepositResponse result = walletService.deposit(uuid, depositRequest);
        return ResponseEntity.ok(result);
    }
}
