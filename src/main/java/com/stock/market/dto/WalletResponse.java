package com.stock.market.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.Set;

public record WalletResponse(@NotBlank String walletId, Set<StockRequestAndResponse> stocks) {}
