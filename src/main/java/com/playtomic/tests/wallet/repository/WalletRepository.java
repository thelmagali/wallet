package com.playtomic.tests.wallet.repository;

import com.playtomic.tests.wallet.model.Wallet;
import java.math.BigDecimal;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    @Query("select w from Wallet w where w.uuid = ?1")
    Optional<Wallet> getByUuid(@NonNull String uuid);

    @Modifying
    @Transactional
    @Query("UPDATE Wallet w SET w.balance = :newBalance WHERE w.id = :walletId AND w.balance = :oldBalance")
    int updateWalletBalance(BigDecimal newBalance, Long walletId, BigDecimal oldBalance);
}
