package ar.edu.utn.frba.dds.filter;

import ar.edu.utn.frba.dds.utils.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  public JwtAuthenticationFilter(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws ServletException, IOException {

    String authHeader = request.getHeader("Authorization");

    // Si no hay token, no hacemos NADA
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      String token = authHeader.substring(7);

      Long userId = Long.valueOf(jwtUtil.getUserIdFromAccessToken(token));

      List<SimpleGrantedAuthority> authorities =
          jwtUtil.getRoleAndPermissions(token).stream()
              .map(SimpleGrantedAuthority::new)
              .toList();

      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(
              userId,      // principal
              null,
              authorities
          );

      SecurityContextHolder.getContext().setAuthentication(authentication);

    } catch (JwtException | IllegalArgumentException e) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
      return;
    }

    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    // Endpoints públicos
    return request.getServletPath().startsWith("/api/auth/");
  }
}

