package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.data.BigDecimalConverter;
import com.playtomic.tests.wallet.data.WalletBalanceOperationDTO;
import com.playtomic.tests.wallet.data.WalletDTO;
import com.playtomic.tests.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequestMapping(path = "wallet")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;
    private final BigDecimalConverter bigDecimalConverter;

    /*
     * I do not understand the purpose of this endpoint.
     * I do not delete it since it is not required, but in a real scenario I would ask for its purpose since, if I am
     * not wrong we could get rid of it.
     * */
    @RequestMapping("/")
    public void log() {
        log.info("Logging from /");
    }

    @GetMapping(
            path = "{walletId}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public WalletRS getWallet(@PathVariable("walletId") final String walletPublicId) {
        final WalletDTO result =  this.walletService.getWalletByWalletPublicId(walletPublicId);
        return this.from(result);
    }

    @PostMapping(
            path = "add-funds",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public WalletRS addFundsToWallet(@RequestBody final AddFundsRQ request) {
        final WalletBalanceOperationDTO balanceOperationDTO = this.from(request);
        final WalletDTO wallet = this.walletService.addFundsToWallet(balanceOperationDTO);
        return this.from(wallet);
    }

    private WalletRS from(final WalletDTO wallet) {
        final String amount = this.bigDecimalConverter.from(wallet.getBalance());
        return WalletRS.builder()
                .walletPublicId(wallet.getWalletPublicId())
                .balance(amount)
                .build();
    }

    private WalletBalanceOperationDTO from(final AddFundsRQ request) {
        final BigDecimal amount = this.bigDecimalConverter.from(request.getAmount());
        return WalletBalanceOperationDTO.builder()
                .walletDTO(WalletDTO.builder()
                        .walletPublicId(request.getWalletPublicId())
                        .build())
                .balanceOperation(request.getOperation())
                .creditCardNumber(request.getCreditCardNumber())
                .amount(amount)
                .build();
    }
}
