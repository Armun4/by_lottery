package com.bynder.lottery.controller.util;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(value = IllegalArgumentException.class)
  ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
    return ResponseEntity.badRequest().body(e.getMessage());
  }
}
