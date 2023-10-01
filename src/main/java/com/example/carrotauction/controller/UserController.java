package com.example.carrotauction.controller;

import com.example.carrotauction.dto.user.LoginRefreshRequestDto;
import com.example.carrotauction.dto.user.LoginRequestDto;
import com.example.carrotauction.dto.user.LoginResponseDto;
import com.example.carrotauction.dto.user.SignUpRequestDto;
import com.example.carrotauction.entity.User;
import com.example.carrotauction.service.UserService;
import javax.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  @PermitAll
  @PostMapping
  public ResponseEntity<String> postSignUp(
      @RequestBody SignUpRequestDto dto
  ) {
    userService.Signup(dto);
    return ResponseEntity.ok("success");
  }

  @GetMapping("/login")
  public ResponseEntity<LoginResponseDto> getLogin(
      @RequestBody LoginRequestDto dto
  ) {
    return ResponseEntity.ok(userService.login(dto));
  }

  @GetMapping("/test")
  public ResponseEntity<User> getTe(
      @AuthenticationPrincipal User user
  ){
    System.out.println(user);
    return ResponseEntity.ok(user);
  }

  @PostMapping("/refresh")
  public ResponseEntity<LoginResponseDto> getRefreshLogin(
      @RequestBody LoginRefreshRequestDto dto
  ){
    return ResponseEntity.ok(userService.refreshLogin(dto));
  }
}
