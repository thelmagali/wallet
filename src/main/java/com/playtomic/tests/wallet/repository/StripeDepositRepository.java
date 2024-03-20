package com.playtomic.tests.wallet.repository;

import com.playtomic.tests.wallet.model.Deposit;
import com.playtomic.tests.wallet.model.StripeDeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StripeDepositRepository extends JpaRepository<StripeDeposit, Long> {
}
