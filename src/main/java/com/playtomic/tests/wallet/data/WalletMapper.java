package com.playtomic.tests.wallet.data;

import com.playtomic.tests.wallet.repository.WalletEntity;
import org.springframework.stereotype.Component;

@Component
public class WalletMapper {
    public WalletDTO from(final WalletEntity wallet) {
        return WalletDTO.builder()
                .walletId(wallet.getWalletId())
                .walletPublicId(wallet.getWalletPublicId())
                .balance(wallet.getBalance())
                .build();
    }
}
