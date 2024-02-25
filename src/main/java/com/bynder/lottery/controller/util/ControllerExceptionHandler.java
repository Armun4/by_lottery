package com.bynder.lottery.controller.util;

import java.util.NoSuchElementException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(value = IllegalArgumentException.class)
  ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
    return ResponseEntity.badRequest().body(e.getMessage());
  }

  @ExceptionHandler(value = NoSuchElementException.class)
  ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e) {
    return ResponseEntity.badRequest().body(e.getMessage());
  }

  @ExceptionHandler(value = RuntimeException.class)
  ResponseEntity<String> handleRuntimeException(RuntimeException e) {
    return ResponseEntity.internalServerError().body(e.getMessage());
  }
}
