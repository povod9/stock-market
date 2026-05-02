package com.stock.market.controller;

import com.stock.market.dto.TradeRequest;
import com.stock.market.dto.WalletResponse;
import com.stock.market.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("wallets")
@Slf4j
@RequiredArgsConstructor
public class WalletController {

  private final WalletService walletService;

  @PostMapping("/{wallet_id}/stocks/{stock_name}")
  public ResponseEntity<?> createTrade(
      @Valid @RequestBody TradeRequest req,
      @PathVariable("wallet_id") String walletId,
      @PathVariable("stock_name") String stockName) {

    walletService.createTrade(req.tradeType(), walletId, stockName);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{wallet_id}")
  public ResponseEntity<WalletResponse> findWallet(@PathVariable("wallet_id") String walletId) {
    WalletResponse walletResponse = walletService.findWallet(walletId);

    return ResponseEntity.ok().body(walletResponse);
  }

  @GetMapping("/{wallet_id}/stocks/{stock_name}")
  public ResponseEntity<Long> findStockInTheWallet(
      @PathVariable("wallet_id") String walletId, @PathVariable("stock_name") String stockName) {

    Long stockQuantity = walletService.findStockInTheWallet(walletId, stockName);

    return ResponseEntity.ok().body(stockQuantity);
  }
}
