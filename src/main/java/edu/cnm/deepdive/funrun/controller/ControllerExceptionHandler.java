package edu.cnm.deepdive.funrun.controller;

import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Handles exceptions thrown by request handling methods in the same controller.
 */
@RestControllerAdvice
public class ControllerExceptionHandler {

  /**
   * Returns exception NOT_Found.
   */
  @ExceptionHandler(NoSuchElementException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource not found")
  public void notFound() {
  }
}
