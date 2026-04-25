package com.stock.market.controller;

import com.stock.market.dto.StockRequest;
import com.stock.market.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stocks")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping
    public ResponseEntity<?> createStock(
            @RequestBody StockRequest stockRequest
    )
    {
        stockService.createStock(stockRequest);
        return ResponseEntity.ok()
                .build();
    }
}
