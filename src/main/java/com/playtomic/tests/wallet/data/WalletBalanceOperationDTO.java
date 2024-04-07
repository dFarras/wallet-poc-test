package com.playtomic.tests.wallet.data;

import com.playtomic.tests.wallet.api.BalanceOperation;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletBalanceOperationDTO {
    private WalletDTO walletDTO;
    private String creditCardNumber;
    private BalanceOperation balanceOperation;
    private BigDecimal amount;
}
