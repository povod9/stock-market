package com.stock.market.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.stock.market.entity.AuditLogEntity;
import com.stock.market.entity.StockEntity;
import com.stock.market.entity.WalletEntity;
import com.stock.market.entity.WalletStockEntity;
import com.stock.market.enums.TradeType;
import com.stock.market.exception.OutOfStockException;
import com.stock.market.repository.AuditLogRepository;
import com.stock.market.repository.StockRepository;
import com.stock.market.repository.WalletRepository;
import com.stock.market.repository.WalletStockRepository;
import java.util.HashSet;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

  @Mock private AuditLogRepository auditLogRepository;
  @Mock private StockRepository stockRepository;
  @Mock private WalletRepository walletRepository;
  @Mock private WalletStockRepository walletStockRepository;

  @InjectMocks private WalletService walletService;

  @InjectMocks private StockService stockService;

  @Test
  void createWalletIfDoesntExists() {
    String walletId = "fdsaf1";
    String stockName = "Apple";
    TradeType tradeType = TradeType.BUY;

    StockEntity stockEntity = new StockEntity(1L, stockName, 10L, 1L);

    when(stockRepository.findByStockName(stockName)).thenReturn(Optional.of(stockEntity));
    when(stockRepository.save(any(StockEntity.class)))
        .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

    when(walletStockRepository.findByWalletIdAndStockName(any(), eq(stockName)))
        .thenReturn(Optional.empty());
    when(walletStockRepository.save(any(WalletStockEntity.class)))
        .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

    when(walletRepository.findByWalletId(walletId)).thenReturn(Optional.empty());
    when(walletRepository.save(any(WalletEntity.class)))
        .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

    when(auditLogRepository.save(any(AuditLogEntity.class)))
        .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

    walletService.createTrade(tradeType, walletId, stockName);

    verify(walletRepository).save(any(WalletEntity.class));
  }

  @Test
  void doesntCreateWalletIfExists() {
    String walletId = "fdsaf1";
    String stockName = "Apple";
    TradeType tradeType = TradeType.BUY;

    StockEntity stockEntity = new StockEntity(1L, stockName, 10L, 1L);

    WalletEntity walletEntity = new WalletEntity(1L, walletId, new HashSet<>());

    WalletStockEntity walletStockEntity =
        new WalletStockEntity(1L, walletEntity, stockName, 0L, 1L);

    when(stockRepository.findByStockName(stockName)).thenReturn(Optional.of(stockEntity));

    when(walletStockRepository.findByWalletIdAndStockName(walletEntity.getId(), stockName))
        .thenReturn(Optional.of(walletStockEntity));

    when(walletRepository.findByWalletId(walletId)).thenReturn(Optional.of(walletEntity));

    when(auditLogRepository.save(any(AuditLogEntity.class)))
        .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

    walletService.createTrade(tradeType, walletId, stockName);

    verify(walletRepository, never()).save(any(WalletEntity.class));
  }

  @Test
  void doesntCreateBuyTradeIfStockQuantityEqZero() {
    String walletId = "fdsaf1";
    String stockName = "Apple";
    TradeType tradeType = TradeType.BUY;

    StockEntity stockEntity = new StockEntity(1L, stockName, 0L, 1L);

    when(stockRepository.findByStockName(stockName)).thenReturn(Optional.of(stockEntity));
    when(walletRepository.save(any(WalletEntity.class)))
        .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

    assertThrows(
        OutOfStockException.class, () -> walletService.createTrade(tradeType, walletId, stockName));

    verify(stockRepository, never()).save(any(StockEntity.class));
    verify(walletStockRepository, never()).save(any(WalletStockEntity.class));
    verify(auditLogRepository, never()).save(any(AuditLogEntity.class));
  }

  @Test
  void doesntCreateSellTradeIfWalletStockQuantityEqZero() {
    String walletId = "fdsaf1";
    String stockName = "Apple";
    TradeType tradeType = TradeType.SELL;

    StockEntity stockEntity = new StockEntity(1L, stockName, 10L, 1L);
    WalletEntity walletEntity = new WalletEntity(1L, walletId, new HashSet<>());
    WalletStockEntity walletStockEntity =
        new WalletStockEntity(1L, walletEntity, stockName, 0L, 1L);

    when(stockRepository.findByStockName(stockName)).thenReturn(Optional.of(stockEntity));
    when(walletRepository.findByWalletId(walletId)).thenReturn(Optional.of(walletEntity));
    when(walletStockRepository.findByWalletIdAndStockName(walletEntity.getId(), stockName))
        .thenReturn(Optional.of(walletStockEntity));

    assertThrows(
        OutOfStockException.class, () -> walletService.createTrade(tradeType, walletId, stockName));

    verify(stockRepository, never()).save(any(StockEntity.class));
    verify(walletStockRepository, never()).save(any(WalletStockEntity.class));
    verify(auditLogRepository, never()).save(any(AuditLogEntity.class));
  }
}
