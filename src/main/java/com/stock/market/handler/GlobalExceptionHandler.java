package com.stock.market.handler;

import com.stock.market.dto.ErrorDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorDto> handleNoSuch(
            NoSuchElementException e
    )
    {
        ErrorDto errorDto = new ErrorDto(
                "No Such Element",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(400)
                .body(errorDto);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDto> handeEntityNot(
            EntityNotFoundException e
    )
    {
        ErrorDto errorDto = new ErrorDto(
                "Not Found",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(404)
                .body(errorDto);
    }

}
