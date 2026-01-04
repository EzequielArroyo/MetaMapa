package ar.edu.utn.frba.dds.exception;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
  private String code;
  private String message;
  private int status;
  private String path;
  private Instant timestamp;
}

