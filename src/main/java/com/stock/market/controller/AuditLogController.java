package com.stock.market.controller;

import com.stock.market.dto.AuditLogResponse;
import com.stock.market.service.AuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping("/log")
    public ResponseEntity<AuditLogResponse> auditLog(){
        AuditLogResponse auditLogResponse = auditLogService.auditLog();

        return ResponseEntity.ok()
                .body(auditLogResponse);
    }
}
