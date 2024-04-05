package com.playtomic.tests.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface WalletRepo extends JpaRepository<WalletEntity, Long> {
    @Transactional(readOnly = true)
    public WalletEntity findByWalletPublicId(final String walletPublicId);
}
