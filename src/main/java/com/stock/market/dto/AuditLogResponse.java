package com.stock.market.dto;

import java.util.List;

public record AuditLogResponse(List<AuditLogDto> log) {}
