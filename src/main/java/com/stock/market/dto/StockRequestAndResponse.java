package com.stock.market.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StockRequestAndResponse(
        @NotBlank String name,
        @NotNull @Min(1) Integer quantity
) {
}
