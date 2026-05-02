package com.stock.market.handler;

import com.stock.market.dto.ErrorDto;
import com.stock.market.exception.OutOfStockException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ErrorDto> handleNoSuchElement(NoSuchElementException e) {
    ErrorDto errorDto = new ErrorDto("No Such Element", e.getMessage(), LocalDateTime.now());

    return ResponseEntity.status(404).body(errorDto);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorDto> handleEntityNotFound(EntityNotFoundException e) {
    ErrorDto errorDto = new ErrorDto("Not Found", e.getMessage(), LocalDateTime.now());

    return ResponseEntity.status(404).body(errorDto);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorDto> handleIllegalArg(IllegalArgumentException e) {
    ErrorDto errorDto = new ErrorDto("Illegal Argument", e.getMessage(), LocalDateTime.now());

    return ResponseEntity.status(400).body(errorDto);
  }

  @ExceptionHandler(OutOfStockException.class)
  public ResponseEntity<ErrorDto> handleOutOfStock(OutOfStockException e) {
    ErrorDto errorDto = new ErrorDto("Out Of Stock", e.getMessage(), LocalDateTime.now());

    return ResponseEntity.status(404).body(errorDto);
  }

  @ExceptionHandler(OptimisticLockException.class)
  public ResponseEntity<ErrorDto> handleOptimisticLock(OptimisticLockException e) {
    ErrorDto errorDto = new ErrorDto("Optimistic Lock", e.getMessage(), LocalDateTime.now());

    return ResponseEntity.status(409).body(errorDto);
  }
}
