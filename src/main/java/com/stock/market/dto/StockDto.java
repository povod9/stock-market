package com.stock.market.dto;

import java.util.List;

public record StockDto(
        List<StockRequestAndResponse> stocks
) {
}
