package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.data.WalletDTO;
import com.playtomic.tests.wallet.data.WalletMapper;
import com.playtomic.tests.wallet.repository.WalletEntity;
import com.playtomic.tests.wallet.repository.WalletRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepo walletRepo;
    private final WalletMapper walletMapper;

    /**
     * I will be adding validations in the service, although it is quite common to have them in the controller (and
     * it would definitely change nothing for this exercise) just to keep in line with hexagonal architecture.
     * I will not be using hibernate validation since that would require me to test it with integration tests, and
     * I do prefer to use unit tests here.
     * */
    /**
     * A very important validation I would add here is to ensure the user has authorization to access to the given
     * wallet and if he or she has access to this method. Again, it would be implemented here to keep in line with
     * hexagonal architecture-
     */
    public WalletDTO getWalletByWalletPublicId(final String walletPublicId) {
        if (Objects.isNull(walletPublicId)) {
            //TODO Implement here an http exception process taking advantage of Spring's controller advices
            throw new RuntimeException("Wallet public id must not be null");
        }
        final WalletEntity wallet = this.walletRepo.findByWalletPublicId(walletPublicId);
        return this.walletMapper.from(wallet);
    }
}
