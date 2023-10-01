package com.example.carrotauction.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {

  private int code;
  private String message;

  public ErrorResponse(ErrorCode errorCode){
    this.code = errorCode.getCode();
    this.message = errorCode.getMessage();
  }
}
