package com.playtomic.tests.wallet.mapper;

import com.playtomic.tests.wallet.api.dto.WalletDTO;
import com.playtomic.tests.wallet.model.Wallet;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class WalletConverter {

    public WalletDTO entityToDto(Wallet wallet) {
        return new WalletDTO(UUID.fromString(wallet.getUuid()), wallet.getBalance());
    }
}
