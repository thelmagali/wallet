package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.api.dto.DepositRequest;
import com.playtomic.tests.wallet.api.dto.DepositResponse;
import com.playtomic.tests.wallet.api.exception.GlobalExceptionHandler;
import com.playtomic.tests.wallet.mapper.DepositConverter;
import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.service.exception.BalanceNotMatchingException;
import com.playtomic.tests.wallet.service.stripe.StripeService;
import com.playtomic.tests.wallet.service.stripe.dto.Payment;
import javax.transaction.Transactional;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DepositService {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final StripeService stripeService;
    private final DepositWriter depositWriter;
    private final WalletService walletService;
    private final DepositConverter depositConverter;

    public DepositService(StripeService stripeService, DepositWriter depositWriter, WalletService walletService, DepositConverter depositConverter) {
        this.stripeService = stripeService;
        this.depositWriter = depositWriter;
        this.walletService = walletService;
        this.depositConverter = depositConverter;
    }

    public DepositResponse deposit(@NonNull String walletUuid, @NonNull DepositRequest depositRequest) {
        final var wallet = walletService.getWallet(walletUuid);
        validateCurrentBalance(wallet, depositRequest);
        final var stripePaymentResponse = stripeService.charge(depositRequest.getCreditCardNumber(), depositRequest.getAmount());
        return registerDepositOrRefundCard(wallet, depositRequest, stripePaymentResponse);
    }

    @Transactional
    public DepositResponse registerDepositOrRefundCard(Wallet wallet, DepositRequest depositRequest, Payment stripePayment) {
        try {
            walletService.topUpBalance(depositRequest, wallet);
            final var savedDeposit = depositWriter.saveDeposit(wallet, depositRequest, stripePayment);
            return depositConverter.entityToResponse(savedDeposit);
        } catch (Exception e) {
            logger.error("Could not register the deposit [WalletId: {}, Amount: {}, StripePaymentId: {}]. Refunding.",
                    wallet.getId(), depositRequest.getAmount(), stripePayment.getId());
            stripeService.refund(stripePayment.getId());
            throw e;
        }
    }

    private void validateCurrentBalance(Wallet wallet, DepositRequest depositRequest) {
        if (!(wallet.getBalance().doubleValue() == depositRequest.getCurrentBalance().doubleValue())) {
            throw new BalanceNotMatchingException(wallet.getUuid(), depositRequest.getCurrentBalance(), wallet.getBalance());
        }
    }
}
