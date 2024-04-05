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
    private final BalanceOperation operation;
    private final BigDecimal amount;

    @JsonCreator
    public AddFundsRQ(
            @JsonProperty("walletId") final String walletPublicId,
            final BalanceOperation operation,
            final BigDecimal amount
    ) {
        this.walletPublicId = walletPublicId;
        this.operation = operation;
        this.amount = amount;
    }
}
