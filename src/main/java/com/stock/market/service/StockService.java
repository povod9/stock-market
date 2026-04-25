package com.stock.market.service;

import com.stock.market.dto.StockDto;
import com.stock.market.dto.StockRequestAndResponse;
import com.stock.market.entity.StockEntity;
import com.stock.market.mapper.StockMapper;
import com.stock.market.repository.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class StockService {

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;

    public StockService(StockRepository stockRepository, StockMapper stockMapper) {
        this.stockRepository = stockRepository;
        this.stockMapper = stockMapper;
    }


    @Transactional
    public void createStock(StockRequestAndResponse stockRequestAndResponse) {

        String finalName = stockRequestAndResponse.name().trim();
        if (stockRequestAndResponse.quantity() <= 0){
            throw new IllegalArgumentException("Quantity cannot be zero or less");
        }

        if(finalName.isEmpty()){
            throw new IllegalArgumentException("Stock name must be non-empty");
        }

        boolean isExists = stockRepository.existsByStockName(finalName);

        if(isExists){
            throw new IllegalArgumentException("Stock with this name already exists: " + finalName);
        }

        StockEntity stockEntity = new StockEntity(
                null,
                finalName,
                stockRequestAndResponse.quantity()
        );

        stockRepository.save(stockEntity);
        log.info("Stock '{}' created with quantity {}", finalName, stockRequestAndResponse.quantity());
    }

    public StockDto findStock() {

        List<StockEntity> stockEntities = stockRepository.findAll();
        List<StockRequestAndResponse> dtoList = stockMapper.listStockToDto(stockEntities);

        return new StockDto(dtoList);
    }
}
