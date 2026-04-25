package com.stock.market.mapper;

import com.stock.market.dto.StockRequestAndResponse;
import com.stock.market.entity.StockEntity;
import com.stock.market.entity.WalletStockEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockMapper {

    StockRequestAndResponse walletStockToDto(WalletStockEntity walletStockEntity);
}
