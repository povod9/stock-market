package com.stock.market.repository;

import com.stock.market.entity.WalletStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletStockRepository extends JpaRepository<WalletStockEntity,Long> {
    Optional<WalletStockEntity> findByWalletIdAndStockName(String walletId, String stockName);
}
