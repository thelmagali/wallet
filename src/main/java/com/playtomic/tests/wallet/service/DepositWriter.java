package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.api.dto.DepositRequest;
import com.playtomic.tests.wallet.model.Deposit;
import com.playtomic.tests.wallet.model.StripeDeposit;
import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.repository.DepositRepository;
import com.playtomic.tests.wallet.repository.StripeDepositRepository;
import com.playtomic.tests.wallet.service.stripe.dto.Payment;
import java.math.BigDecimal;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class DepositWriter {
    private final DepositRepository depositRepository;
    private final StripeDepositRepository stripeDepositRepository;

    public DepositWriter(DepositRepository depositRepository, StripeDepositRepository stripeDepositRepository) {
        this.depositRepository = depositRepository;
        this.stripeDepositRepository = stripeDepositRepository;
    }

    @Transactional
    public Deposit saveDeposit(Wallet wallet, DepositRequest depositRequest, Payment stripePayment) {
        final var internalDeposit = saveInternalDeposit(depositRequest.getAmount(), wallet.getId());
        saveStripeDeposit(depositRequest, stripePayment.getId(), internalDeposit);
        return internalDeposit;
    }

    private void saveStripeDeposit(DepositRequest depositRequest, String paymentId, Deposit savedDeposit) {
        final var stripeDeposit = new StripeDeposit(savedDeposit, paymentId, depositRequest.getCreditCardNumber());
        stripeDepositRepository.save(stripeDeposit);
    }

    private Deposit saveInternalDeposit(BigDecimal amount, Long walletId) {
        final var deposit = new Deposit(amount, walletId);
        return depositRepository.save(deposit);
    }
}
