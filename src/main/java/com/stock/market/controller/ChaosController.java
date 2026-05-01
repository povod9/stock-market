package com.stock.market.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ChaosController {

  @PostMapping("/chaos")
  public ResponseEntity<?> chaosKill() {
    log.warn("Chaos called, killing current instance");
    System.exit(1);
    return ResponseEntity.ok().build();
  }
}
