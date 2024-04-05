package com.playtomic.tests.wallet.data;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletDTO {
    private Long walletId;
    private String walletPublicId;
    private BigDecimal balance;
}
