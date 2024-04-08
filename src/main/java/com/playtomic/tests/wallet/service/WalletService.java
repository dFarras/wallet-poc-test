package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.api.BalanceOperation;
import com.playtomic.tests.wallet.data.WalletBalanceOperationDTO;
import com.playtomic.tests.wallet.data.WalletDTO;
import com.playtomic.tests.wallet.data.WalletMapper;
import com.playtomic.tests.wallet.exceptions.ErrorCatalog;
import com.playtomic.tests.wallet.repository.WalletEntity;
import com.playtomic.tests.wallet.repository.WalletRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.ResourceAccessException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepo walletRepo;
    private final WalletMapper walletMapper;
    private final PaymentGateway paymentGateway;

    /**
     * I will be adding validations in the service, although it is quite common to have them in the controller (and
     * it would definitely change nothing for this exercise) just to keep in line with hexagonal architecture.
     * A very important validation I would add here is to ensure the user has authorization to access to the given
     * wallet and if he or she has access to this method. Again, it would be implemented here to keep in line with
     * hexagonal architecture
     */
    @Transactional(readOnly = true)
    public WalletDTO getWalletByWalletPublicId(
            @NotBlank final String walletPublicId
    ) {
        final WalletEntity wallet = this.walletRepo.findByWalletPublicId(walletPublicId);
        if (Objects.isNull(wallet)) {
            throw ErrorCatalog.RESOURCE_NOT_FOUND.getException();
        }
        return this.walletMapper.from(wallet);
    }

    @Transactional(
            isolation = Isolation.REPEATABLE_READ,
            propagation = Propagation.REQUIRES_NEW
    )
    public WalletDTO addFundsToWallet(
            @Valid final WalletBalanceOperationDTO balanceOperationDTO
    ) {
        Assert.isTrue(BalanceOperation.ADD.equals(balanceOperationDTO.getBalanceOperation()), "");

        final String walletPublicId = balanceOperationDTO.getWalletDTO().getWalletPublicId();
        final WalletEntity wallet = this.walletRepo.findByWalletPublicId(walletPublicId);
        if (Objects.isNull(wallet)) {
            throw ErrorCatalog.RESOURCE_NOT_FOUND.getException();
        }

        this.chargeCreditCard(balanceOperationDTO);

        wallet.setBalance(wallet.getBalance().add(balanceOperationDTO.getAmount()));
        final WalletEntity result = this.walletRepo.save(wallet);

        return this.walletMapper.from(result);
    }

    private void chargeCreditCard(final WalletBalanceOperationDTO balanceOperationDTO) {
        try {
            final Payment payment = this.paymentGateway.charge(
                    balanceOperationDTO.getCreditCardNumber(),
                    balanceOperationDTO.getAmount()
            );
            /*
             * I see the payment carries an id. I will log it, but it must definitely be stored in a database
             * (with or without the indirection of an event).
             * Credit card number is not logged since it would be against GDPR, I am not 100% sure about this point, but
             * it is better to be extra careful on these cases.
             */
            log.info("Payment performed with id: {}",
                    payment.getId()
            );
        } catch (final ResourceAccessException resourceAccessException) {
            log.error("Error reading remote");
            throw ErrorCatalog.PAYMENT_GATEWAY_UNAVAILABLE.getException();
        } catch (final Exception exception) {
            log.error("Something unexpected happened while charging credit card!!");
            throw ErrorCatalog.INTERNAL_SERVER_ERROR.getException();
        }
    }

    private static boolean isMoreThanOneWalletModified(List<WalletEntity> rows) {
        return Objects.isNull(rows) || rows.size() > 1;
    }
}
