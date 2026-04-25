package com.stock.market.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "wallets")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String walletId;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL)
    private List<WalletStockEntity> stocks;
}
