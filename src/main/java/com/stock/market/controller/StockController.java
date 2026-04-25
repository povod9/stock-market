package com.stock.market.controller;

import com.stock.market.dto.StockDto;
import com.stock.market.dto.StockRequestAndResponse;
import com.stock.market.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/stocks")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping
    public ResponseEntity<?> createStock(@RequestBody StockRequestAndResponse stockRequestAndResponse) {
        stockService.createStock(stockRequestAndResponse);
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping
    public ResponseEntity<StockDto> findStock(){
        StockDto stocks = stockService.findStock();
        return ResponseEntity.ok(stocks);
    }
}
