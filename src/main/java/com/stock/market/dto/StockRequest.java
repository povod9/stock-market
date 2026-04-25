package com.stock.market.dto;

public record StockRequest(
        String stockName,
        Integer quantity
) {
}
