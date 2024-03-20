package com.playtomic.tests.wallet.repository;

import com.playtomic.tests.wallet.model.Deposit;
import com.playtomic.tests.wallet.model.Wallet;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long> {
}
