package com.stock.market.dto;

import java.util.Set;

public record WalletResponse(
        String walletId,
        Set<StockRequestAndResponse> stocks
) {
}
