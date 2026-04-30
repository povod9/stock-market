package com.stock.market.service;

import com.stock.market.dto.AuditLogResponse;
import com.stock.market.dto.StockRequestAndResponse;
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
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private AuditLogRepository auditLogRepository;
    @Mock
    private StockRepository stockRepository;
    @Mock
    private WalletRepository walletRepository;
    @Mock
    private WalletStockRepository walletStockRepository;

    @InjectMocks
    private WalletService walletService;

    @InjectMocks
    private StockService stockService;

    @Test
    @Order(1)
    void createStock(){
        StockRequestAndResponse stock = new StockRequestAndResponse(
                "Apple",
                10
        );

        stockService.createStock(stock);
        verify(stockRepository.save(any()));
    }

    @Test
    @Order(2)
    void createTrade() {
        String walletId = "fdsaf1";
        String stockName = "Apple";
        Type type = Type.BUY;

        StockEntity stockEntity = new StockEntity(
                1L,
                stockName,
                10
        );

        when(stockRepository.findByStockName(stockName)).thenReturn(Optional.of(stockEntity));
        when(stockRepository.save(any(StockEntity.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        when(walletStockRepository.findByWalletIdAndStockName(any(),eq(stockName))).thenReturn(Optional.empty());
        when(walletStockRepository.save(any(WalletStockEntity.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        when(walletRepository.findByWalletId(walletId)).thenReturn(Optional.empty());
        when(walletRepository.save(any(WalletEntity.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        when(auditLogRepository.save(any(AuditLogEntity.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        walletService.createTrade(type,walletId,stockName);

        verify(walletRepository).save(any(WalletEntity.class));

    }
}