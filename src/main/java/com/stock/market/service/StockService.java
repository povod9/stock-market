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

        String name = stockRequestAndResponse.name();

        boolean isExists = stockRepository.existsByStockName(name);

        if(isExists){
            throw new IllegalArgumentException("Stock with this name already exists: " + name);
        }

        StockEntity stockEntity = new StockEntity(
                null,
                name,
                stockRequestAndResponse.quantity()
        );

        stockRepository.save(stockEntity);
        log.info("Stock '{}' created with quantity {}", name, stockRequestAndResponse.quantity());
    }

    public StockDto findStock() {

        List<StockEntity> stockEntities = stockRepository.findAll();
        log.info("Found '{}' stock in bank", stockEntities.size());
        List<StockRequestAndResponse> dtoList = stockMapper.listStockToDto(stockEntities);

        return new StockDto(dtoList);
    }
}
