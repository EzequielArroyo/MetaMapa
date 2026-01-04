package ar.edu.utn.frba.dds.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({
      ExpiredJwtException.class,
      MalformedJwtException.class,
      UnsupportedJwtException.class,
      IllegalArgumentException.class,
      InvalidTokenTypeException.class,
      InvalidTokenException.class,
  })
  public ResponseEntity<ErrorResponse> handleJwtErrors(
      Exception ex,
      HttpServletRequest request
  ) {
    return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body(new ErrorResponse(
            "INVALID_TOKEN",
            "Invalid or expired token",
            401,
            request.getRequestURI(),
            Instant.now()
        ));
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDenied(
      HttpServletRequest request
  ) {
    return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body(new ErrorResponse(
            "ACCESS_DENIED",
            "You do not have permission to access this resource",
            403,
            request.getRequestURI(),
            Instant.now()
        ));
  }
}
