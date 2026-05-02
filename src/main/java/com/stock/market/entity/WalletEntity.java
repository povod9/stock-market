package com.stock.market.entity;

import jakarta.persistence.*;
import java.util.Set;
import lombok.*;
import org.hibernate.annotations.NaturalId;

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

  @NaturalId private String walletId;

  @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL)
  private Set<WalletStockEntity> stocks;
}
