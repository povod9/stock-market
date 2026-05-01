package com.stock.market.dto;

import com.stock.market.enums.TradeType;

public record AuditLogDto(TradeType tradeType, String walletId, String stockName) {}
