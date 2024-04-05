package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.data.WalletMapper;
import com.playtomic.tests.wallet.repository.WalletEntity;
import com.playtomic.tests.wallet.repository.WalletRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {
    private static final String WALLET_PUBLIC_ID = "WALLET_PUBLIC_ID";
    private static final WalletEntity WALLET_ENTITY = WalletEntity.builder()
            .walletId(1L)
            .walletPublicId(WALLET_PUBLIC_ID)
            .balance(new BigDecimal("10.0"))
            .build();
    @Mock
    private WalletRepo walletRepo;
    /**
     * I personally never use @Spy since unit tests, in my personal opinion, should be using mocks instead.
     * But in the case of mappers I do an exception, although it does not mean that mapper MUST have their own unit
     * tests also
     * */
    @Spy
    private WalletMapper walletMapper;
    private WalletService walletService;

    @BeforeEach
    public void setup() {
        this.walletService = new WalletService(
                this.walletRepo,
                this.walletMapper
        );
    }

    @Test
    void whenAValidWalletPublicIdIsUsedAndItDoesExistThenTheWalletIsReturned() {
        when(this.walletRepo.findByWalletPublicId(any())).thenReturn(WALLET_ENTITY);

        this.walletService.getWalletByWalletPublicId(WALLET_PUBLIC_ID);

        verify(this.walletRepo, times(1)).findByWalletPublicId(WALLET_PUBLIC_ID);
        verify(this.walletMapper, times(1)).from(WALLET_ENTITY);
    }

    @Test
    void whenANullWalletPublicIdIsProvidedThenAnExceptionIsThrown() {
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> this.walletService.getWalletByWalletPublicId(null)
        );
    }
}