package com.stock.market.service;

import com.stock.market.dto.StockDto;
import com.stock.market.dto.StockRequestAndResponse;
import com.stock.market.entity.StockEntity;
import com.stock.market.mapper.StockMapper;
import com.stock.market.repository.StockRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockService {

  private final StockRepository stockRepository;
  private final StockMapper mapper;

  @Transactional
  public void createStock(StockRequestAndResponse stockRequestAndResponse) {

    String name = stockRequestAndResponse.name();
    stockRepository
        .findByStockName(name)
        .map(
            stockEntity -> {
              stockEntity.setQuantity(
                  stockEntity.getQuantity() + stockRequestAndResponse.quantity());
              return stockRepository.save(stockEntity);
            })
        .orElseGet(
            () -> {
              StockEntity stockEntity =
                  new StockEntity(null, name, stockRequestAndResponse.quantity(), null);
              return stockRepository.save(stockEntity);
            });
    log.info(
        "Stock '{}' created or updated, quantity {}", name, stockRequestAndResponse.quantity());
  }

  public StockDto findStock() {

    List<StockEntity> stockEntities = stockRepository.findAll();
    log.info("Found '{}' stock in bank", stockEntities.size());
    List<StockRequestAndResponse> dtoList = mapper.listStockToDto(stockEntities);

    return new StockDto(dtoList);
  }
}
