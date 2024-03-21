package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.api.dto.DepositRequest;
import com.playtomic.tests.wallet.api.dto.WalletDTO;
import com.playtomic.tests.wallet.mapper.WalletConverter;
import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.service.exception.WalletNotFoundException;
import javax.transaction.Transactional;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class WalletService {
    private final WalletRepository walletRepository;
    private final WalletConverter walletConverter;

    public WalletService(WalletRepository walletRepository, WalletConverter walletConverter) {
        this.walletRepository = walletRepository;
        this.walletConverter = walletConverter;
    }

    public WalletDTO getWalletDTO(@NonNull String uuid) {
        final var walletEntity = getWallet(uuid);
        return walletConverter.entityToDto(walletEntity);
    }

    public Wallet getWallet(@NonNull String uuid) {
        return walletRepository.getByUuid(uuid)
                .orElseThrow(() -> new WalletNotFoundException(uuid));
    }

    @Transactional
    public void topUpBalance(DepositRequest depositRequest, Wallet wallet) {
        final var newBalance = wallet.getBalance().add(depositRequest.getAmount());
        walletRepository.updateWalletBalance(newBalance, wallet.getId(), wallet.getBalance());
    }
}
