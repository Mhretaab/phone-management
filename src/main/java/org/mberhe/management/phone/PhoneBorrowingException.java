package org.mberhe.management.phone;

public class PhoneBorrowingException extends RuntimeException {
  private final String message;

  public PhoneBorrowingException() {
    this.message = "";
  }

  public PhoneBorrowingException(String message) {
    super(message);
    this.message = message;
  }
}
