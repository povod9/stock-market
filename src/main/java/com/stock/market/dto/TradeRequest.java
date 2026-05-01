package com.stock.market.dto;

import com.stock.market.enums.TradeType;
import jakarta.validation.constraints.NotNull;

public record TradeRequest(@NotNull TradeType tradeType) {}
