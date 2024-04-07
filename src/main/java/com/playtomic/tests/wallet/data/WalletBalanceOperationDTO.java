package com.playtomic.tests.wallet.data;

import com.playtomic.tests.wallet.api.BalanceOperation;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletBalanceOperationDTO {
    @Valid
    @NotNull
    private WalletDTO walletDTO;
    @NotBlank
    private String creditCardNumber;
    @NotNull
    private BalanceOperation balanceOperation;
    @DecimalMin("0.0")
    private BigDecimal amount;
}
