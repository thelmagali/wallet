package com.playtomic.tests.wallet.mapper;

import com.playtomic.tests.wallet.api.dto.WalletResponse;
import com.playtomic.tests.wallet.model.Wallet;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class WalletConverter {

    public WalletResponse entityToDto(Wallet wallet) {
        return new WalletResponse(UUID.fromString(wallet.getUuid()), wallet.getBalance());
    }
}
