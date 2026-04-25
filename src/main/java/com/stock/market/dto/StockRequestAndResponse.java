package com.stock.market.dto;

public record StockRequestAndResponse(
        String stockName,
        Integer quantity
) {
}
