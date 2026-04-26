package com.stock.market.controller;

import com.stock.market.dto.TradeRequest;
import com.stock.market.dto.WalletResponse;
import com.stock.market.service.WalletService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("wallets")
@Slf4j
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/{wallet_id}/stocks/{stock_name}")
    public ResponseEntity<?> createTrade(
            @Valid @RequestBody TradeRequest req,
            @Valid @PathVariable("wallet_id") String walletId,
            @Valid @PathVariable("stock_name") String stockName) {

        walletService.createTrade(req.type(), walletId, stockName);
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/{wallet_id}")
    public ResponseEntity<WalletResponse> findWallet(@Valid @PathVariable("wallet_id") String walletId) {
        WalletResponse walletResponse = walletService.findWallet(walletId);

        return ResponseEntity.ok()
                .body(walletResponse);
    }

    @GetMapping("/{wallet_id}/stocks/{stock_name}")
    public ResponseEntity<Integer> findStockInTheWallet(
            @Valid @PathVariable("wallet_id") String walletId,
            @Valid @PathVariable("stock_name") String stockName) {

        Integer stockQuantity = walletService.findStockInTheWallet(walletId, stockName);

        return ResponseEntity.ok()
                .body(stockQuantity);
    }

}
