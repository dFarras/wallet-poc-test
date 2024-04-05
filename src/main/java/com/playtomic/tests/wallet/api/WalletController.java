package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.data.WalletDTO;
import com.playtomic.tests.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "wallet")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    /**
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
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public AddFundsRS addFundsToWallet(final AddFundsRQ request) {
     return null;
    }

    //Create tests, we can add one no happy path since we now stripe service fails on funds less than 10€
    //add endpoint to add funds to wallet

    private WalletRS from(WalletDTO wallet) {
        return WalletRS.builder()
                .walletPublicId(wallet.getWalletPublicId())
                .balance(wallet.getBalance())
                .build();
    }
}
