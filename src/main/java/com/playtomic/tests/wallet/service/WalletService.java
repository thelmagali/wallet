package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.dto.WalletDTO;
import com.playtomic.tests.wallet.mapper.WalletConverter;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.service.exception.WalletNotFoundException;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class WalletService {
    private final WalletRepository walletRepository;
    private final WalletConverter walletConverter;

    public WalletService(final WalletRepository walletRepository, final WalletConverter walletConverter) {
        this.walletRepository = walletRepository;
        this.walletConverter = walletConverter;
    }

    public WalletDTO getWallet(@NonNull String uuid) {
        return walletRepository.getByUuid(uuid)
                .map(walletConverter::entityToDto)
                .orElseThrow(() -> new WalletNotFoundException(uuid));
    }
}
