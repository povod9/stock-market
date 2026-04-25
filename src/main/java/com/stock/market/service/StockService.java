package com.stock.market.service;

import com.stock.market.dto.StockRequest;
import com.stock.market.entity.StockEntity;
import com.stock.market.repository.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }


    @Transactional
    public void createStock(
            StockRequest stockRequest
    )
    {

        if (stockRequest.quantity() <= 0){
            throw new IllegalArgumentException("Quantity cannot be zero or less");
        }

        if(stockRequest.stockName() == null || stockRequest.stockName().trim().isEmpty()){
            throw new IllegalArgumentException("Stock name must be non-empty");
        }

        boolean isExists = stockRepository.existsByStockName(stockRequest.stockName());

        if(isExists){
            throw new IllegalArgumentException("Stock with this name already exists: " + stockRequest.stockName());
        }

        StockEntity stockEntity = new StockEntity(
                null,
                stockRequest.stockName(),
                stockRequest.quantity()
        );

        stockRepository.save(stockEntity);
        log.info("Stock '{}' created with quantity {}", stockRequest.stockName(), stockRequest.quantity());
    }
}
