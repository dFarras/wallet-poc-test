package com.playtomic.tests.wallet.repository;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletEntity {
    @Id
    @Column(name = "WALLET_ID")
    private Long walletId;
    @Column(name = "WALLET_PUBLIC_ID", nullable = false)
    private String walletPublicId;
    @Column(name = "BALANCE", nullable = false)
    private BigDecimal balance;
}
