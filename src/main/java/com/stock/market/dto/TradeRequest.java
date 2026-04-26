package com.stock.market.dto;

import com.stock.market.enums.Type;
import jakarta.validation.constraints.NotNull;

public record TradeRequest (
        @NotNull Type type
){
}
