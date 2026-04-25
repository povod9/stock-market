package com.stock.market.repository;

import com.stock.market.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<WalletEntity,Long> {
    Optional<WalletEntity> findByWalletId(String walletId);
}
