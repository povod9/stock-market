package com.stock.market.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "wallet_stock")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletStockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private WalletEntity wallet;

    @Column(nullable = false)
    private String stockName;

    @Column(nullable = false)
    private Integer quantity;
}
