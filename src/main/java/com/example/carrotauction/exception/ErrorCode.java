package com.example.carrotauction.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
  ID_NOT_FOUND(400,"ID NOT FOUND"),
  ID_REGISTERED(400, "ID REGISTERED"),
  PW_NOT_MATCH(400,"PASSWORD NOT MATCH"),
  TOKEN_TIME_OUT(403, "TOKEN IS TIME OUT"),
  NON_TOKEN(401,"NON TOKEN"),
  INVALID_TOKEN(401,"INVALID_TOKEN"),
  UNKNOWN_ERROR(500, "UNKNOWN_ERROR")
  ;


  private int code;
  private String message;
}
