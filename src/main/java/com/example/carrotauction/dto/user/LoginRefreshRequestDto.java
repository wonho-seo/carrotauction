package com.example.carrotauction.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRefreshRequestDto {

  private String accessToken;
  private String refreshToken;
}
