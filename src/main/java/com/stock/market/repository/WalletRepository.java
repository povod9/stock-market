package com.stock.market.repository;

import com.stock.market.entity.WalletEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<WalletEntity, Long> {
  Optional<WalletEntity> findByWalletId(String walletId);
}
