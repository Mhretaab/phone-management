package org.mberhe.management.common.exceptions.handler;

import org.springframework.http.HttpStatus;

public record ExceptionRule(Class<?> exceptionClass, HttpStatus status) {
}
