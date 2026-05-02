package com.stock.market.repository;

import com.stock.market.entity.WalletStockEntity;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletStockRepository extends JpaRepository<WalletStockEntity, Long> {
  @Lock(LockModeType.OPTIMISTIC)
  Optional<WalletStockEntity> findByWalletIdAndStockName(Long walletId, String stockName);
}
