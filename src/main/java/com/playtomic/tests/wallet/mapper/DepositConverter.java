package com.playtomic.tests.wallet.mapper;

import com.playtomic.tests.wallet.api.dto.DepositResponse;
import com.playtomic.tests.wallet.model.Deposit;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class DepositConverter {

    public DepositResponse entityToResponse(Deposit deposit, UUID walletUuid) {
        return new DepositResponse(UUID.fromString(deposit.getUuid()), walletUuid, deposit.getAmount());
    }
}
