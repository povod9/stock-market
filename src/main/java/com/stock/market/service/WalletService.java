package com.stock.market.service;

import com.stock.market.dto.TradeRequest;
import com.stock.market.entity.AuditLogEntity;
import com.stock.market.entity.StockEntity;
import com.stock.market.entity.WalletEntity;
import com.stock.market.entity.WalletStockEntity;
import com.stock.market.enums.Type;
import com.stock.market.repository.AuditLogRepository;
import com.stock.market.repository.StockRepository;
import com.stock.market.repository.WalletRepository;
import com.stock.market.repository.WalletStockRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class WalletService {

    private final StockRepository stockRepository;
    private final WalletRepository walletRepository;
    private final AuditLogRepository auditLogRepository;
    private final WalletStockRepository walletStockRepository;

    public WalletService(StockRepository stockRepository, WalletRepository walletRepository, AuditLogRepository auditLogRepository, WalletStockRepository walletStockRepository) {
        this.stockRepository = stockRepository;
        this.walletRepository = walletRepository;
        this.auditLogRepository = auditLogRepository;
        this.walletStockRepository = walletStockRepository;
    }


    @Transactional
    public void createTrade(
            Type type,
            String walletId,
            String stockName
    )
    {
        String finalWalletId = walletId.trim();
        String finalStockName = stockName.trim();

        if(stockName.isEmpty()){
            throw new IllegalArgumentException("Stock name must be non-empty");
        }

        if(walletId.isEmpty()){
            throw new IllegalArgumentException("Wallet name must be non-empty");
        }

        StockEntity stockEntity = stockRepository.findByStockName(finalStockName)
                .orElseThrow(() -> new EntityNotFoundException("Stock with this name doesn't exist: " + finalStockName));

        WalletEntity walletEntity = walletRepository.findByWalletId(finalWalletId)
                .orElseGet(() -> {
                    WalletEntity w = new WalletEntity();
                    w.setWalletId(finalWalletId);
                    w.setStocks(new HashSet<>());
                    return walletRepository.save(w);
                });
        log.info("Create new wallet with wallet_id='{}'", walletEntity.getWalletId());

        if(type.equals(Type.BUY)) {

            if (stockEntity.getQuantity() == 0){
                log.warn("Cannot buy, no stock in the bank for stockName='{}', quantity requested=1", stockEntity.getStockName());
                throw new NoSuchElementException("There is no stock in the bank buy");
            }
            WalletStockEntity walletStockEntity = walletStockRepository.findByWalletIdAndStockName(finalWalletId,finalStockName)
                    .orElseGet(() -> {
                        WalletStockEntity newWalletStock = new WalletStockEntity(
                                null,
                                walletEntity,
                                finalStockName,
                                0
                        );
                        return walletStockRepository.save(newWalletStock);
                    });
            walletEntity.getStocks().add(walletStockEntity);

            walletStockEntity.setQuantity(walletStockEntity.getQuantity() + 1);
            stockEntity.setQuantity(stockEntity.getQuantity() - 1);

            stockRepository.save(stockEntity);
            walletStockRepository.save(walletStockEntity);
            log.info("BUY: wallet='{}', stock='{}'", finalWalletId, finalStockName);

        }else {

            WalletStockEntity walletStockEntity = walletStockRepository.findByWalletIdAndStockName(finalWalletId,finalStockName)
                    .orElseThrow(() -> new EntityNotFoundException("Cannot find walletStock by:" + finalWalletId + " and " + finalStockName));

            if(walletStockEntity.getQuantity() == 0){
                log.warn("Cannot buy, no stock in the wallet for stockName='{}', quantity requested=1", stockEntity.getStockName());
                throw new NoSuchElementException("There is no stock in the wallet sell");
            }

            walletStockEntity.setQuantity(walletStockEntity.getQuantity() - 1);
            stockEntity.setQuantity(stockEntity.getQuantity() + 1);

            if(walletStockEntity.getQuantity() == 0){
                walletStockRepository.delete(walletStockEntity);
                walletEntity.getStocks().remove(walletStockEntity);
            }else {
                walletStockRepository.save(walletStockEntity);
            }

            stockRepository.save(stockEntity);
        }

        AuditLogEntity createAuditLog = new AuditLogEntity(
                null,
                type,
                finalWalletId,
                finalStockName
        );

        auditLogRepository.save(createAuditLog);
        log.info("Create audit, type='{}', wallet='{}', stock='{}'", type, finalWalletId, finalStockName);
    }
}
