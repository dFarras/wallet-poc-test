package com.playtomic.tests.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface WalletRepo extends JpaRepository<WalletEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_READ)
    public WalletEntity findByWalletPublicId(final String walletPublicId);
}
