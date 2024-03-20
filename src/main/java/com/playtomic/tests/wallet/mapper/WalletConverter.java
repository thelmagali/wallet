package com.playtomic.tests.wallet.mapper;

import com.playtomic.tests.wallet.dto.WalletDTO;
import com.playtomic.tests.wallet.model.Wallet;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class WalletConverter {

    public WalletDTO entityToDto(Wallet wallet) {
        return new WalletDTO(UUID.fromString(wallet.getUuid()), wallet.getBalance());
    }

    public Wallet dtoToEntity(WalletDTO dto) {
        Wallet wallet = new Wallet();
        wallet.setUuid(dto.getUuid().toString());
        wallet.setBalance(dto.getBalance());
        return wallet;
    }
}
