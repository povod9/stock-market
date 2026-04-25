package com.stock.market.dto;

public record StockRequestAndResponse(
        String name,
        Integer quantity
) {
}
