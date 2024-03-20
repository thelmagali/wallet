package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.api.dto.DepositRequest;
import com.playtomic.tests.wallet.api.dto.DepositResponse;
import com.playtomic.tests.wallet.api.dto.WalletDTO;
import com.playtomic.tests.wallet.mapper.WalletConverter;
import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.service.stripe.StripeService;
import com.playtomic.tests.wallet.service.stripe.dto.Payment;
import com.playtomic.tests.wallet.service.exception.WalletNotFoundException;
import javax.transaction.Transactional;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class WalletService {
    private final WalletRepository walletRepository;
    private final WalletConverter walletConverter;
    private final StripeService stripeService;
    private final DepositService depositService;

    public WalletService(WalletRepository walletRepository, WalletConverter walletConverter, StripeService stripeService, DepositService depositService) {
        this.walletRepository = walletRepository;
        this.walletConverter = walletConverter;
        this.stripeService = stripeService;
        this.depositService = depositService;
    }

    public WalletDTO getWallet(@NonNull String uuid) {
        final var walletEntity = getWalletEntity(uuid);
        return walletConverter.entityToDto(walletEntity);
    }

    public DepositResponse deposit(@NonNull String walletUuid, @NonNull DepositRequest depositRequest) {
        final var stripePaymentResponse = stripeService.charge(depositRequest.getCreditCardNumber(), depositRequest.getAmount());
        return registerDepositOrRefundCard(walletUuid, depositRequest, stripePaymentResponse);
    }

    @Transactional
    public DepositResponse registerDepositOrRefundCard(String walletUuid, DepositRequest depositRequest, Payment stripePayment) {
        try {
            final var wallet = getWalletEntity(walletUuid);
            updateWalletBalance(depositRequest, wallet);
            return depositService.registerDeposit(wallet, depositRequest, stripePayment);
        } catch (Exception e) {
            stripeService.refund(stripePayment.getId());
            throw e;
        }
    }

    @Transactional
    public void updateWalletBalance(DepositRequest depositRequest, Wallet wallet) {
        final var newBalance = wallet.getBalance().add(depositRequest.getAmount());
        walletRepository.updateWalletBalance(newBalance, wallet.getId(), wallet.getBalance());
    }


    private Wallet getWalletEntity(@NonNull String uuid) {
        return walletRepository.getByUuid(uuid)
                .orElseThrow(() -> new WalletNotFoundException(uuid));
    }
}
