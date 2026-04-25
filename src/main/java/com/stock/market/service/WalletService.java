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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.NoSuchElementException;

@Service
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
        StockEntity stockEntity = stockRepository.findByStockName(stockName)
                .orElseThrow(() -> new EntityNotFoundException("Stock with this name doesn't exist: " + stockName));

        WalletEntity walletEntity = walletRepository.findByWalletId(walletId)
                .orElseGet(() -> {
                    WalletEntity w = new WalletEntity();
                    w.setWalletId(walletId);
                    w.setStocks(new ArrayList<>());
                    return walletRepository.save(w);
                });

        walletRepository.save(walletEntity);

        if(type.equals(Type.BUY)) {
            if (stockEntity.getQuantity() == 0){
                throw new NoSuchElementException("There is no stock in the bank buy");
            }
            WalletStockEntity walletStockEntity = walletStockRepository.findByWalletIdAndStockName(walletId,stockName)
                    .orElseGet(() -> {
                        WalletStockEntity newWalletStock = new WalletStockEntity(
                                null,
                                walletEntity,
                                stockName,
                                1
                        );
                        return walletStockRepository.save(newWalletStock);
                    });

            walletStockEntity.setQuantity(walletStockEntity.getQuantity() + 1);
            stockEntity.setQuantity(stockEntity.getQuantity() - 1);

            stockRepository.save(stockEntity);
            walletStockRepository.save(walletStockEntity);
        }else {
            WalletStockEntity walletStockEntity = walletStockRepository.findByWalletIdAndStockName(walletId,stockName)
                    .orElseThrow(() -> new EntityNotFoundException("Cannot find walletStock by:" + walletId + " and " + stockName));

            if(walletStockEntity.getQuantity() == 0){
                throw new NoSuchElementException("There is no stock in the wallet sell");
            }

            walletStockEntity.setQuantity(walletStockEntity.getQuantity() - 1);
            stockEntity.setQuantity(stockEntity.getQuantity() + 1);

            if(walletStockEntity.getQuantity() == 0){
                walletStockRepository.delete(walletStockEntity);
            }

            stockRepository.save(stockEntity);
            walletStockRepository.save(walletStockEntity);
        }

        AuditLogEntity createAuditLog = new AuditLogEntity(
                null,
                type,
                walletId,
                stockName
        );

        auditLogRepository.save(createAuditLog);
    }
}
