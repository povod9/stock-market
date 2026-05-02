package com.stock.market.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "wallet_stock")
@Getter
@Setter
@EqualsAndHashCode
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
  private Long quantity;

  @Column @Version private Long version;
}
