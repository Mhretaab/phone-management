package org.mberhe.management.common.exceptions;

public class EntityAlreadyExistsException extends RuntimeException {
  private final String message;

  public EntityAlreadyExistsException() {
    this.message = "";
  }

  public EntityAlreadyExistsException(String message) {
    super(message);
    this.message = message;
  }
}