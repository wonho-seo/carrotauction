package com.example.carrotauction.config.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;

  @Value("${auth.jwt.type}")
  private String JWT_TYPE = "Bearer";

  @Value("${auth.field}")
  private String AUTHENTICATION_FIELD = "Authorization";

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    if (request.getHeader(AUTHENTICATION_FIELD) != null){
      String[] authentication = request.getHeader(AUTHENTICATION_FIELD).split(" ");
      if (authentication[0].equals(JWT_TYPE)) {
        if (authentication.length == 2 && jwtTokenProvider.validateToken(authentication[1])) {
          Authentication auth = jwtTokenProvider.getAuthentication(authentication[1]);

          SecurityContextHolder.getContext().setAuthentication(auth);
        }
      }
    }

    filterChain.doFilter(request, response);

  }
}