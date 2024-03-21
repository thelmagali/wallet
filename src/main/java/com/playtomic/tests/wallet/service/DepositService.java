package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.api.dto.DepositRequest;
import com.playtomic.tests.wallet.api.dto.DepositResponse;
import com.playtomic.tests.wallet.api.exception.GlobalExceptionHandler;
import com.playtomic.tests.wallet.model.Wallet;
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

    public DepositService(StripeService stripeService, DepositWriter depositWriter, WalletService walletService) {
        this.stripeService = stripeService;
        this.depositWriter = depositWriter;
        this.walletService = walletService;
    }

    public DepositResponse deposit(@NonNull String walletUuid, @NonNull DepositRequest depositRequest) {
        final var wallet = walletService.getWallet(walletUuid);
        final var stripePaymentResponse = stripeService.charge(depositRequest.getCreditCardNumber(), depositRequest.getAmount());
        return registerDepositOrRefundCard(wallet, depositRequest, stripePaymentResponse);
    }

    @Transactional
    public DepositResponse registerDepositOrRefundCard(Wallet wallet, DepositRequest depositRequest, Payment stripePayment) {
        try {
            walletService.topUpBalance(depositRequest, wallet);
            return depositWriter.saveDeposit(wallet, depositRequest, stripePayment);
        } catch (Exception e) {
            logger.error("Could not register the deposit [WalletId: {}, Amount: {}, StripePaymentId: {}]. Refunding.",
                    wallet.getId(), depositRequest.getAmount(), stripePayment.getId());
            stripeService.refund(stripePayment.getId());
            throw e;
        }
    }
}
