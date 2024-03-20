package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.api.dto.DepositRequest;
import com.playtomic.tests.wallet.api.dto.DepositResponse;
import com.playtomic.tests.wallet.mapper.DepositConverter;
import com.playtomic.tests.wallet.model.Deposit;
import com.playtomic.tests.wallet.model.StripeDeposit;
import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.repository.DepositRepository;
import com.playtomic.tests.wallet.repository.StripeDepositRepository;
import com.playtomic.tests.wallet.service.stripe.dto.Payment;
import java.math.BigDecimal;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class DepositService {
    private final DepositRepository depositRepository;
    private final StripeDepositRepository stripeDepositRepository;
    private final DepositConverter depositConverter;

    public DepositService(DepositRepository depositRepository, StripeDepositRepository stripeDepositRepository, DepositConverter depositConverter) {
        this.depositRepository = depositRepository;
        this.stripeDepositRepository = stripeDepositRepository;
        this.depositConverter = depositConverter;
    }

    @Transactional
    public DepositResponse registerDeposit(Wallet wallet, DepositRequest depositRequest, Payment stripePayment) {
        final var savedDeposit = saveDeposit(depositRequest.getAmount(), wallet.getId());
        saveStripeDeposit(depositRequest, stripePayment.getId(), savedDeposit);
        return depositConverter.entityToResponse(savedDeposit, UUID.fromString(wallet.getUuid()));
    }

    private void saveStripeDeposit(DepositRequest depositRequest, String paymentId, Deposit savedDeposit) {
        final var stripeDeposit = new StripeDeposit(savedDeposit, paymentId, depositRequest.getCreditCardNumber());
        stripeDepositRepository.save(stripeDeposit);
    }

    private Deposit saveDeposit(BigDecimal amount, Long walletId) {
        final var deposit = new Deposit(amount, walletId);
        return depositRepository.save(deposit);
    }
}
