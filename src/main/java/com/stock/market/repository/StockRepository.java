package com.stock.market.repository;

import com.stock.market.entity.StockEntity;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Long> {

  @Lock(LockModeType.OPTIMISTIC)
  Optional<StockEntity> findByStockName(String stockName);
}
