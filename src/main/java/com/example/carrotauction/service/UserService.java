package com.example.carrotauction.service;

import com.example.carrotauction.config.security.JwtTokenProvider;
import com.example.carrotauction.dto.user.LoginRefreshRequestDto;
import com.example.carrotauction.dto.user.LoginRequestDto;
import com.example.carrotauction.dto.user.LoginResponseDto;
import com.example.carrotauction.dto.user.SignUpRequestDto;
import com.example.carrotauction.entity.User;
import com.example.carrotauction.exception.CustomException;
import com.example.carrotauction.exception.ErrorCode;
import com.example.carrotauction.repository.UserRepository;
import com.example.carrotauction.type.UserRole;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final JwtTokenProvider jwtTokenProvider;

  public void Signup(SignUpRequestDto dto) {
    userRepository.save(User.builder()
        .userId(dto.getUserId())
        .password(passwordEncoder.encode(dto.getPassword()))
        .email(dto.getEmail())
        .money(1000)
        .roles(List.of(UserRole.USER_ROLE))
        .image(null)
        .nickname(dto.getNickname())
        .build());
  }

  public LoginResponseDto login(LoginRequestDto dto) {
    User user = userRepository.findByUserId(dto.getUserId()).orElse(new User());

    if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
      throw new CustomException(ErrorCode.PW_NOT_MATCH.getMessage(), ErrorCode.PW_NOT_MATCH);
    }
    String atk = jwtTokenProvider.createAccessToken(dto.getUserId(),
        user.getRoles().stream().map(Enum::toString).collect(
            Collectors.toList()));
    String rtk = jwtTokenProvider.createRefreshToken(dto.getUserId(),
        user.getRoles().stream().map(Enum::toString).collect(
            Collectors.toList()));

    return LoginResponseDto.builder()
        .accessToken(atk)
        .refreshToken(rtk)
        .build();
  }

  public LoginResponseDto refreshLogin(LoginRefreshRequestDto dto) {
    if (!jwtTokenProvider.validateToken(dto.getRefreshToken()) || !jwtTokenProvider.validateToken(
        dto.getAccessToken())) {
      throw new CustomException(ErrorCode.INVALID_TOKEN.getMessage(), ErrorCode.INVALID_TOKEN);
    }

    Authentication authentication = jwtTokenProvider.getAuthentication(dto.getAccessToken());
    if (authentication.getName() != jwtTokenProvider.getAuthentication(dto.getRefreshToken())
        .getName()) {
      throw new CustomException(ErrorCode.INVALID_TOKEN.getMessage(), ErrorCode.INVALID_TOKEN);
    }

    if (jwtTokenProvider.getExpiration(dto.getRefreshToken()) < 0) {
      throw new CustomException(ErrorCode.TOKEN_TIME_OUT.getMessage(), ErrorCode.TOKEN_TIME_OUT);
    }

    User user = userRepository.findByUserId(authentication.getName()).orElseThrow(
        () -> new CustomException(ErrorCode.ID_NOT_FOUND.getMessage(), ErrorCode.ID_NOT_FOUND));

    return LoginResponseDto.builder()
        .accessToken(jwtTokenProvider.createAccessToken(user.getUserId(), user.getRoles().stream().map(Enum::name).collect(
            Collectors.toList())))
        .refreshToken(jwtTokenProvider.createRefreshToken(user.getUserId(), user.getRoles().stream().map(Enum::name).collect(
            Collectors.toList())))
        .build();
  }
}
