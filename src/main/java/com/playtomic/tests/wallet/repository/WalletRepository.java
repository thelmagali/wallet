package com.playtomic.tests.wallet.repository;

import com.playtomic.tests.wallet.model.Wallet;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    @Query("select w from Wallet w where w.uuid = ?1")
    Optional<Wallet> getByUuid(@NonNull String uuid);
}
