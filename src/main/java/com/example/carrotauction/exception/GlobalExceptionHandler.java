package com.example.carrotauction.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex){
    ErrorResponse response = new ErrorResponse(ex.getErrorCode());
    return ResponseEntity.status(ex.getErrorCode().getCode()).body(response);
  }
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(CustomException ex){
    ErrorResponse response = new ErrorResponse(ex.getErrorCode());
    return ResponseEntity.status(ex.getErrorCode().getCode()).body(response);
  }
}
