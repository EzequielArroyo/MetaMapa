package ar.edu.utn.frba.dds.exception;

import ar.edu.utn.frba.dds.controllers.AuthController;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = AuthController.class)
public class AuthExceptionHandler {

  @ExceptionHandler({
      EntityNotFoundException.class,
      InvalidCredentialsException.class,
      UserBlockedException.class,
  })
  public ResponseEntity<ErrorResponse> handleAuthErrors(
      Exception ex,
      HttpServletRequest request
  ) {

    ErrorResponse error = new ErrorResponse(
    "INVALID_CREDENTIALS",
    "Invalid credentials",
    HttpStatus.UNAUTHORIZED.value(),
    request.getRequestURI(),
    Instant.now()
    );

    return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body(error);
  }
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidation(
      MethodArgumentNotValidException ex,
      HttpServletRequest request
  ) {
    String msg = ex.getBindingResult().getFieldErrors().stream()
        .map(e -> e.getField() + ": " + e.getDefaultMessage())
        .findFirst()
        .orElse("Invalid request");

    return ResponseEntity.badRequest().body(
        new ErrorResponse(
            "VALIDATION_ERROR",
            msg,
            400,
            request.getRequestURI(),
            Instant.now()
        )
    );
  }

}


