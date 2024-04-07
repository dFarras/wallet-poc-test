package com.playtomic.tests.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface WalletRepo extends JpaRepository<WalletEntity, Long> {
    @Transactional(readOnly = true)
    public WalletEntity findByWalletPublicId(final String walletPublicId);

    @Modifying
    @Query(
            value = "WITH UPDATED_WALLETS AS (" +
                        "UPDATE WALLET " +
                        "SET(BALANCE=BALANCE + :FUNDS) " +
                        "WHERE WALLET_ID=:WALLET_ID)" +
                    "SELECT * " +
                    "FROM UPDATED_WALLETS",
            nativeQuery = true
    )
    public List<WalletEntity> addFundsToWallet(
            @Param("FUNDS") final BigDecimal amount,
            @Param("WALLET_ID") final Long walletId
    );
}
