package ar.edu.utn.frba.dds.exception;

public class UserBlockedException extends RuntimeException {

  public UserBlockedException() {
    super("User is block");
  }
}

