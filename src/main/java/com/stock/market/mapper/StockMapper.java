package com.stock.market.mapper;

import com.stock.market.dto.StockRequestAndResponse;
import com.stock.market.entity.StockEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StockMapper {

    @Mapping(source = "stockName", target = "name")
    StockRequestAndResponse stockToDto(StockEntity stockEntity);

    List<StockRequestAndResponse> listStockToDto(List<StockEntity> stockEntities);
}
