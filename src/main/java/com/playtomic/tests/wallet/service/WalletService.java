package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.api.BalanceOperation;
import com.playtomic.tests.wallet.data.WalletBalanceOperationDTO;
import com.playtomic.tests.wallet.data.WalletDTO;
import com.playtomic.tests.wallet.data.WalletMapper;
import com.playtomic.tests.wallet.repository.WalletEntity;
import com.playtomic.tests.wallet.repository.WalletRepo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepo walletRepo;
    private final WalletMapper walletMapper;
    private final StripeService stripeService;

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

    public WalletDTO addFundsToWallet(final WalletBalanceOperationDTO balanceOperationDTO) {
        //TODO move this validation to DTO through hibernate validation
        Assert.notNull(balanceOperationDTO.getCreditCardNumber());
        Assert.notNull(balanceOperationDTO, "");
        Assert.notNull(balanceOperationDTO.getBalanceOperation(), "");
        Assert.isTrue(BalanceOperation.ADD.equals(balanceOperationDTO.getBalanceOperation()), "");
        Assert.notNull(balanceOperationDTO.getAmount());
        Assert.isTrue(balanceOperationDTO.getAmount().compareTo(BigDecimal.ZERO) > 0, "");


        //TODO extract this to a new bean in order to abstract this complexity
        //TODO maybe move this to RestTemplate error handler where it should live
        try {
            final Payment payment = this.stripeService.charge(
                    balanceOperationDTO.getCreditCardNumber(),
                    balanceOperationDTO.getAmount()
            );
        } catch (final StripeAmountTooSmallException stripeAmountTooSmallException) {
            //TODO StripeAmountTooSmallException on 422 (less than 10€)

        } catch (final HttpClientErrorException httpClientErrorException) {
            final HttpStatus statusCode = httpClientErrorException.getStatusCode();
            if (statusCode.is4xxClientError()) {
                //TODO HttpClientErrorException carrying an statuscode 400
                log.error("Stripe failed due to an invalid request");
                throw new RuntimeException(); //TODO create specific exception
            } else if (statusCode.is5xxServerError()) {
                //TODO HttpClientErrorException carrying an status code 500
                log.error("Stripe failed unexpectedly");
                throw new RuntimeException(); //TODO create specific exception
                //we could retry since it could be something temporal
            } else {
                log.error("An unexpected error raised an exception");
                throw new RuntimeException(); //TODO create specific exception
            }
        } catch (final ResourceAccessException resourceAccessException) {
            //TODO ResourceAccessException when error reading from remote
            log.error("Log we had an error reading remote");
            throw new RuntimeException(); //TODO create specific exception
            //could be retried
        } catch (final Exception exception) {
            log.error("Log something unexpected happened while charging credit card!!");
            throw new RuntimeException(); //TODO create specific exception
        }
        //TODO doubt, just one wallet is expected. What happens if the query returns more than one element? It is reverted?
        final List<WalletEntity> rows = this.walletRepo.addFundsToWallet(
                balanceOperationDTO.getAmount(),
                balanceOperationDTO.getWalletDTO().getWalletId()
        );
        if(Objects.isNull(rows) || rows.size() != 1) {//TODO extract to a method
            /**
             * I will be logging wallet information, but in a real scenario it would be better to move this information
             * as an event to a topic so that I can be properly handled.
             * */
            log.error("More than one operation were modified!");
        }
        return this.walletMapper.from(rows.get(0));
    }
}