package ar.edu.utn.frba.dds.security;

import ar.edu.utn.frba.dds.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException
  ) throws IOException {

    ErrorResponse error = new ErrorResponse(
        "INVALID_TOKEN",
        "Invalid or expired token",
        HttpServletResponse.SC_UNAUTHORIZED,
        request.getRequestURI(),
        Instant.now()
    );

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");
    objectMapper.writeValue(response.getOutputStream(), error);
  }
}

