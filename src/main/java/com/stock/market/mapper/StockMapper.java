package com.stock.market.mapper;

import com.stock.market.dto.StockRequestAndResponse;
import com.stock.market.entity.StockEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockMapper {

  @Mapping(source = "stockName", target = "name")
  StockRequestAndResponse stockToRequestAndResponse(StockEntity stockEntity);

  List<StockRequestAndResponse> listStockToDto(List<StockEntity> stockEntities);
}
