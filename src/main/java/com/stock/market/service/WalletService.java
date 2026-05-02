package com.stock.market.service;

import com.stock.market.dto.WalletResponse;
import com.stock.market.entity.AuditLogEntity;
import com.stock.market.entity.StockEntity;
import com.stock.market.entity.WalletEntity;
import com.stock.market.entity.WalletStockEntity;
import com.stock.market.enums.TradeType;
import com.stock.market.exception.OutOfStockException;
import com.stock.market.mapper.WalletMapper;
import com.stock.market.repository.AuditLogRepository;
import com.stock.market.repository.StockRepository;
import com.stock.market.repository.WalletRepository;
import com.stock.market.repository.WalletStockRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class WalletService {

  private final StockRepository stockRepository;
  private final WalletRepository walletRepository;
  private final AuditLogRepository auditLogRepository;
  private final WalletStockRepository walletStockRepository;
  private final WalletMapper mapper;

  @Transactional
  @Retryable(
      value = {OptimisticLockException.class},
      maxAttempts = 10,
      backoff = @Backoff(delay = 100))
  public void createTrade(TradeType tradeType, String walletId, String stockName) {

    try {
      StockEntity stockEntity =
          stockRepository
              .findByStockName(stockName)
              .orElseThrow(
                  () ->
                      new EntityNotFoundException(
                          "Stock with this name doesn't exist: " + stockName));

      WalletEntity walletEntity =
          walletRepository.findByWalletId(walletId).orElseGet(() -> createAndLogWallet(walletId));

      Long walletPkId = walletEntity.getId();
      if (tradeType.equals(TradeType.BUY)) {

        if (stockEntity.getQuantity() == 0) {
          log.warn(
              "Cannot buy, no stock in the bank for stockName='{}', quantity requested=1",
              stockEntity.getStockName());
          throw new OutOfStockException("There is no stock in the bank buy");
        }
        WalletStockEntity walletStockEntity =
            walletStockRepository
                .findByWalletIdAndStockName(walletPkId, stockName)
                .orElseGet(
                    () -> {
                      WalletStockEntity newWalletStock =
                          new WalletStockEntity(null, walletEntity, stockName, 0L, null);
                      return walletStockRepository.save(newWalletStock);
                    });
        walletEntity.getStocks().add(walletStockEntity);

        walletStockEntity.setQuantity(walletStockEntity.getQuantity() + 1);
        stockEntity.setQuantity(stockEntity.getQuantity() - 1);

        stockRepository.save(stockEntity);
        walletStockRepository.save(walletStockEntity);
        log.info("BUY: wallet='{}', stock='{}'", walletId, stockName);

      } else {

        WalletStockEntity walletStockEntity =
            walletStockRepository
                .findByWalletIdAndStockName(walletPkId, stockName)
                .orElseThrow(
                    () ->
                        new EntityNotFoundException(
                            "Cannot find walletStock by: %s and %s"
                                .formatted(walletId, stockName)));

        if (walletStockEntity.getQuantity() == 0) {
          log.warn(
              "Cannot buy, no stock in the wallet for stockName='{}', quantity requested=1",
              stockEntity.getStockName());
          throw new OutOfStockException("There is no stock in the wallet sell");
        }

        walletStockEntity.setQuantity(walletStockEntity.getQuantity() - 1);
        stockEntity.setQuantity(stockEntity.getQuantity() + 1);

        if (walletStockEntity.getQuantity() == 0) {
          walletStockRepository.delete(walletStockEntity);
          log.info("Stock record deleted from wallet '{}' as quantity reached 0", walletId);
          walletEntity.getStocks().remove(walletStockEntity);
        } else {
          walletStockRepository.save(walletStockEntity);
        }

        stockRepository.save(stockEntity);
      }

      AuditLogEntity createAuditLog = new AuditLogEntity(null, tradeType, walletId, stockName);

      auditLogRepository.save(createAuditLog);
      log.info("Create audit, type='{}', wallet='{}', stock='{}'", tradeType, walletId, stockName);
    } catch (OptimisticLockException e) {
      log.warn("OptimisticLockException!");
    }
  }

  public WalletResponse findWallet(String walletId) {

    WalletEntity walletEntity =
        walletRepository
            .findByWalletId(walletId)
            .orElseThrow(() -> new EntityNotFoundException("Cannot find wallet=" + walletId));

    return mapper.walletToResponse(walletEntity);
  }

  public Long findStockInTheWallet(String walletId, String stockName) {

    WalletEntity walletEntity =
        walletRepository
            .findByWalletId(walletId)
            .orElseThrow(() -> new EntityNotFoundException("Cannot find wallet=" + walletId));

    Long walletPkId = walletEntity.getId();

    WalletStockEntity walletStockEntity =
        walletStockRepository
            .findByWalletIdAndStockName(walletPkId, stockName)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "Cannot find walletStock by: %s and %s".formatted(walletId, stockName)));

    return walletStockEntity.getQuantity();
  }

  private WalletEntity createAndLogWallet(String walletId) {
    WalletEntity walletEntity = new WalletEntity();
    walletEntity.setWalletId(walletId);
    walletEntity.setStocks(new HashSet<>());
    WalletEntity saved = walletRepository.save(walletEntity);
    log.info("Create new wallet with wallet_id='{}'", saved.getWalletId());
    return saved;
  }
}
