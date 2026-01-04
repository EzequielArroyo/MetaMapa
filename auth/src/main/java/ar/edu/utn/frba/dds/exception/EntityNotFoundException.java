package ar.edu.utn.frba.dds.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {

  private final String entity;

  public EntityNotFoundException(String entity) {
    super(entity + " not found");
    this.entity = entity;
  }

}

