package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.api.dto.DepositRequest;
import com.playtomic.tests.wallet.api.dto.DepositResponse;
import com.playtomic.tests.wallet.api.dto.WalletResponse;
import com.playtomic.tests.wallet.service.DepositService;
import com.playtomic.tests.wallet.service.WalletService;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {
    private final WalletService walletService;
    private final DepositService depositService;

    public WalletController(WalletService walletService, DepositService depositService) {
        this.walletService = walletService;
        this.depositService = depositService;
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<WalletResponse> getWalletByUuid(@PathVariable String uuid) {
        WalletResponse walletResponse = walletService.getWalletDTO(uuid);
        return ResponseEntity.ok(walletResponse);
    }

    @PostMapping("/{uuid}/deposits")
    public ResponseEntity<DepositResponse> depositAmount(@PathVariable String uuid, @RequestBody @Valid DepositRequest depositRequest) {
        DepositResponse result = depositService.deposit(uuid, depositRequest);
        return ResponseEntity.ok(result);
    }
}
