package com.playtomic.tests.wallet.data;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletDTO {
    @Min(0)
    private Long walletId;
    @NotBlank
    private String walletPublicId;
    private BigDecimal balance;
}
