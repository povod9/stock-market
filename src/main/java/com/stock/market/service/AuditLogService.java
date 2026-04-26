package com.stock.market.service;

import com.stock.market.dto.AuditLogDto;
import com.stock.market.dto.AuditLogResponse;
import com.stock.market.mapper.AuditLogMapper;
import com.stock.market.repository.AuditLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    public AuditLogService(AuditLogRepository auditLogRepository, AuditLogMapper auditLogMapper) {
        this.auditLogRepository = auditLogRepository;
        this.auditLogMapper = auditLogMapper;
    }

    public AuditLogResponse auditLog() {
        log.info("Requesting audit logs, limit: 10,000");
        List<AuditLogDto> auditLogDtoList = auditLogRepository.findAll().stream()
                .map(auditLogMapper::auditEntityToDto)
                .limit(10_000L)
                .toList();

        return new AuditLogResponse(auditLogDtoList);
    }
}
