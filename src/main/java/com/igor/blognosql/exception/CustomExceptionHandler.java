package com.igor.blognosql.exception;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

  @ExceptionHandler(OptimisticLockingFailureException.class)
  public ResponseEntity<Object> handleException(OptimisticLockingFailureException ex) {
    Error error = new Error(HttpStatus.CONFLICT.value(),
        "Error: this document was already changed by another user. Please try again");
    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleException(Exception ex) {
    Error error = new Error(HttpStatus.BAD_REQUEST.value(), ex.getLocalizedMessage());
    ex.printStackTrace();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  private record Error(int statusCode, String message) {
  }
}
