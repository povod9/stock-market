package com.stock.market.service;

import com.stock.market.dto.AuditLogDto;
import com.stock.market.dto.AuditLogResponse;
import com.stock.market.mapper.AuditLogMapper;
import com.stock.market.repository.AuditLogRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuditLogService {

  private final AuditLogRepository auditLogRepository;
  private final AuditLogMapper mapper;

  public AuditLogResponse auditLog() {
    log.info("Requesting audit logs, limit: 10,000");
    List<AuditLogDto> auditLogDtoList =
        auditLogRepository.findAll().stream().map(mapper::auditEntityToDto).limit(10_000L).toList();

    return new AuditLogResponse(auditLogDtoList);
  }
}
