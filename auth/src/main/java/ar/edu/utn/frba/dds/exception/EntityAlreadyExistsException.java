package ar.edu.utn.frba.dds.exception;

import lombok.Getter;

@Getter
public class EntityAlreadyExistsException extends RuntimeException {

  private final String field;

  public EntityAlreadyExistsException(String field) {
    super("Entity already exists");
    this.field = field;
  }

}

