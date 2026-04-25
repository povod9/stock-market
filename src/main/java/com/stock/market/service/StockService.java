package com.stock.market.service;

import com.stock.market.dto.StockRequestAndResponse;
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
    public void createStock(StockRequestAndResponse stockRequestAndResponse) {

        if (stockRequestAndResponse.quantity() <= 0){
            throw new IllegalArgumentException("Quantity cannot be zero or less");
        }

        if(stockRequestAndResponse.stockName() == null || stockRequestAndResponse.stockName().trim().isEmpty()){
            throw new IllegalArgumentException("Stock name must be non-empty");
        }

        boolean isExists = stockRepository.existsByStockName(stockRequestAndResponse.stockName());

        if(isExists){
            throw new IllegalArgumentException("Stock with this name already exists: " + stockRequestAndResponse.stockName());
        }

        StockEntity stockEntity = new StockEntity(
                null,
                stockRequestAndResponse.stockName(),
                stockRequestAndResponse.quantity()
        );

        stockRepository.save(stockEntity);
        log.info("Stock '{}' created with quantity {}", stockRequestAndResponse.stockName(), stockRequestAndResponse.quantity());
    }
}
