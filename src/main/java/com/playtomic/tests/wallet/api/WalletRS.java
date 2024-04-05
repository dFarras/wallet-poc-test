package com.playtomic.tests.wallet.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
public class WalletRS {
    @JsonProperty("walletId")
    private final String walletPublicId;
    private final BigDecimal balance;
}
