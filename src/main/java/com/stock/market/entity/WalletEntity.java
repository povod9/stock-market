package com.stock.market.entity;

import jakarta.persistence.*;
import java.util.Set;
import lombok.*;

@Entity
@Table(name = "wallets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WalletEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String walletId;

  @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL)
  private Set<WalletStockEntity> stocks;
}
