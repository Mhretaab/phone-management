package org.mberhe.management.common.exceptions;

public class EntityNotFoundException extends RuntimeException {
  private final String message;

  public EntityNotFoundException() {
    this.message = "";
  }

  public EntityNotFoundException(String message) {
    super(message);
    this.message = message;
  }
}