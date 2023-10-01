package com.example.carrotauction.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

  @Value("${auth.jwt.rtk}")
  private String REFRESH_TOKEN;

  @Value("${auth.jwt.secret-key}")
  private String secretKey;

  private final UserDetailsService userDetailsService;

  private final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 30; // 30m
  private final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 14; // 2week

  @PostConstruct
  private void init() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
  }

  public String createAccessToken(String userId, List<String> roles) {
    Claims claims = Jwts.claims().setSubject(userId);
    claims.put("roles", roles);

    Date now = new Date();
    String token = Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION))
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
    return token;
  }

  public String createRefreshToken(String userId, List<String> roles) {
    Claims claims = Jwts.claims().setSubject(userId);
    claims.put("roles", roles);

    Date now = new Date();
    String token = Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION))
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
    return token;
  }

  @Transactional
  public Authentication getAuthentication(String token) { // token 정보 검사

    UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUser(token));

    return new UsernamePasswordAuthenticationToken(userDetails, "",
        userDetails.getAuthorities());
  }

  public String getUser(String token) {
    String info = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody()
        .getSubject();
    return info;
  }

  public boolean validateToken(String token) {
    try { // check validate  with secretKey
      Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
      return !claims.getBody().getExpiration().before(new Date());
    } catch (Exception e) {
      return false;
    }
  }

  public Long getExpiration(String token) {
    Date expiration = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody()
        .getExpiration();
    long now = new Date().getTime();
    return expiration.getTime() - now;
  }
}
