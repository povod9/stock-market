package com.stock.market.repository;

import com.stock.market.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<StockEntity,Long> {
    Optional<StockEntity> findByStockName(String stockName);
}
