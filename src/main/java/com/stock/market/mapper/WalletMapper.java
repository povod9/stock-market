package com.stock.market.mapper;

import com.stock.market.dto.WalletResponse;
import com.stock.market.entity.WalletEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = WalletStockMapper.class)
public interface WalletMapper {

    WalletResponse walletToDto(WalletEntity walletEntity);
}
