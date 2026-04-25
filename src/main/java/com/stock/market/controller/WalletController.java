package com.stock.market.controller;

import com.stock.market.dto.TradeRequest;
import com.stock.market.dto.WalletResponse;
import com.stock.market.service.WalletService;
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
            @RequestBody TradeRequest req,
            @PathVariable("wallet_id") String walletId,
            @PathVariable("stock_name") String stock_name) {

        walletService.createTrade(req.type(), walletId, stock_name);
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/{wallet_id}")
    public ResponseEntity<WalletResponse> findWallet(@PathVariable("wallet_id") String walletId) {
        WalletResponse walletResponse = walletService.findWallet(walletId);

        return ResponseEntity.ok()
                .body(walletResponse);
    }


}
