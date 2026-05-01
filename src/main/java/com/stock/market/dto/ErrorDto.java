package com.stock.market.dto;

import java.time.LocalDateTime;

public record ErrorDto(String message, String errorMessage, LocalDateTime time) {}
