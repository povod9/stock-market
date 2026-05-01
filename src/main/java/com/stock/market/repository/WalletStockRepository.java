package com.stock.market.repository;

import com.stock.market.entity.WalletStockEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletStockRepository extends JpaRepository<WalletStockEntity, Long> {
  Optional<WalletStockEntity> findByWalletIdAndStockName(Long walletId, String stockName);
}
