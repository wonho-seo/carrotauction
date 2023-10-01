package com.example.carrotauction.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class SignUpRequestDto {

  private String userId;

  private String email;

  private String password;

  private String nickname;
}
