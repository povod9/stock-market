package com.stock.market.dto;

import com.stock.market.enums.Type;

public record AuditLogDto(
        Type type,
        String walletId,
        String stockName
) {
}
