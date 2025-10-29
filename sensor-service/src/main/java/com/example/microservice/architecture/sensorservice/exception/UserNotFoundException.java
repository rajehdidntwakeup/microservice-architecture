package com.example.microservice.architecture.sensorservice.exception;

import java.io.Serial;

public class UserNotFoundException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 1L;

  public UserNotFoundException(long id) {
    super("User with id " + id + " not found");
  }
}
