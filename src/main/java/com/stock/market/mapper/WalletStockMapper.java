package com.stock.market.mapper;

import com.stock.market.dto.StockRequestAndResponse;
import com.stock.market.entity.WalletStockEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WalletStockMapper {

    @Mapping(source = "stockName", target = "name")
    StockRequestAndResponse walletStockToDto(WalletStockEntity walletStockEntity);
}
