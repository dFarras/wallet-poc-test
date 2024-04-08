package com.playtomic.tests.wallet.repository;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@Table(name = "WALLET")
@NoArgsConstructor
@AllArgsConstructor
public class WalletEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "WALLET_ID")
    private Long walletId;
    @Column(name = "WALLET_PUBLIC_ID", nullable = false)
    private String walletPublicId;
    @Column(name = "BALANCE", nullable = false)
    private BigDecimal balance;
}

