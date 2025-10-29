package com.example.microservice.architecture.sensorservice.exception;

import java.io.Serial;

public class SensorNotFoundException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 1L;

  public SensorNotFoundException(long id) {
    super("Sensor with id" + id + " not found");
  }
}
