package ar.edu.utn.frba.dds.exception;

public class InvalidTokenTypeException extends RuntimeException {
  public InvalidTokenTypeException() {
    super("Tipo de token inválido");
  }
}

