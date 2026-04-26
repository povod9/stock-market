package com.stock.market.service;

import com.stock.market.mapper.WalletMapper;
import com.stock.market.dto.WalletResponse;
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

import java.util.HashSet;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class WalletService {

    private final StockRepository stockRepository;
    private final WalletRepository walletRepository;
    private final AuditLogRepository auditLogRepository;
    private final WalletStockRepository walletStockRepository;
    private final WalletMapper walletMapper;

    public WalletService(StockRepository stockRepository, WalletRepository walletRepository, AuditLogRepository auditLogRepository, WalletStockRepository walletStockRepository, WalletMapper walletMapper) {
        this.stockRepository = stockRepository;
        this.walletRepository = walletRepository;
        this.auditLogRepository = auditLogRepository;
        this.walletStockRepository = walletStockRepository;
        this.walletMapper = walletMapper;
    }


    @Transactional
    public void createTrade(Type type, String walletId, String stockName) {

        String finalWalletId = validateWalletId(walletId);
        String finalStockName = validateStockName(stockName);

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

        Long walletPkId = walletEntity.getId();
        if(type.equals(Type.BUY)) {

            if (stockEntity.getQuantity() == 0){
                log.warn("Cannot buy, no stock in the bank for stockName='{}', quantity requested=1", stockEntity.getStockName());
                throw new NoSuchElementException("There is no stock in the bank buy");
            }
            WalletStockEntity walletStockEntity = walletStockRepository.findByWalletIdAndStockName(walletPkId,finalStockName)
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

            WalletStockEntity walletStockEntity = walletStockRepository.findByWalletIdAndStockName(walletPkId,finalStockName)
                    .orElseThrow(() -> new EntityNotFoundException("Cannot find walletStock by:"
                            + finalWalletId + " and " + finalStockName));

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

    public WalletResponse findWallet(String walletId) {

        String finalWalletId = validateWalletId(walletId);

        WalletEntity walletEntity = walletRepository.findByWalletId(finalWalletId)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find wallet=" + finalWalletId) );

        return walletMapper.walletToDto(walletEntity);
    }

    public Integer findStockInTheWallet(String walletId, String stockName) {

        String finalWalletId = validateWalletId(walletId);
        String finalStockName = validateStockName(stockName);

        WalletEntity walletEntity = walletRepository.findByWalletId(finalWalletId)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find wallet=" + finalWalletId));

        Long walletPkId = walletEntity.getId();

        WalletStockEntity walletStockEntity = walletStockRepository.findByWalletIdAndStockName(walletPkId, finalStockName)
                .orElseThrow(() ->  new EntityNotFoundException("Cannot find walletStock by:"
                        + finalWalletId + " and " + finalStockName));

        return walletStockEntity.getQuantity();
    }


    public static String validateWalletId(String walletId){
        if(walletId == null)
            throw new IllegalArgumentException("Wallet id must be non-null");
        String finalWalletId = walletId.trim();
        if(finalWalletId.isEmpty())
            throw new IllegalArgumentException("Wallet id must be non-empty");
        return finalWalletId;
    }

    public static String validateStockName(String stockName){
        if (stockName == null)
            throw new IllegalArgumentException("Stock name must be non-null");
        String finalStockName = stockName.trim();
        if(finalStockName.isEmpty())
            throw new IllegalArgumentException("Stock name must be non-empty");
        return finalStockName;
    }
}
