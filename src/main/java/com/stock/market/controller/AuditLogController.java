package com.stock.market.controller;

import com.stock.market.dto.AuditLogResponse;
import com.stock.market.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuditLogController {

  private final AuditLogService auditLogService;

  @GetMapping("/logs")
  public ResponseEntity<AuditLogResponse> auditLog() {
    AuditLogResponse auditLogResponse = auditLogService.auditLog();

    return ResponseEntity.ok().body(auditLogResponse);
  }
}
