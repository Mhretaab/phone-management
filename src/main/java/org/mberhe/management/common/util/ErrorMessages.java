package org.mberhe.management.common.util;

public final class ErrorMessages {
  public static final String USER_ALREADY_EXISTS_ERROR_MSG = "User with email %s or phone number %s already exists.";
  public static final String USER_NOT_FOUND_ERROR_MSG = "User with ID %d not found.";
  public static final String PHONE_ALREADY_EXISTS_ERROR_MSG = "Phone with brand %s and model %s already exists";
  public static final String PHONE_NOT_FOUND_ERROR_MSG = "Phone with ID %d not found.";
  public static final String PHONE_NOT_FOUND_BY_BRAND_MODEL_ERROR_MSG = "Phone with brand %s and model %s not found";

  public static String buildErrorMessage(String template, Object... vars) {
    return String.format(template, vars);
  }

  private ErrorMessages() {
  }
}