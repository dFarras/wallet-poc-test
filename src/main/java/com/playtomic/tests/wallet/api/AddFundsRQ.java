package com.playtomic.tests.wallet.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * This class should be a record in Java 21
 * */
@Getter
public class AddFundsRQ {
    private final String walletPublicId;
    private final String creditCardNumber;
    private final BalanceOperation operation;
    private final BigDecimal amount;

    @JsonCreator
    public AddFundsRQ(
            @JsonProperty("walletId") final String walletPublicId,
            final String creditCardNumber,
            final BalanceOperation operation,
            final String amount
    ) {
        this.walletPublicId = walletPublicId;
        this.creditCardNumber = creditCardNumber;
        this.operation = operation;
        this.amount = new BigDecimal(amount);
    }
}
