package com.example.microservice.architecture.sensorservice.dto.request;

import java.time.LocalDateTime;

public class MeasurementRequestDto {
  private Long sensorId;
  private LocalDateTime timestamp;
  private double temperature;
  private double humidity;

  public MeasurementRequestDto() {}

  public MeasurementRequestDto(Long sensorId, LocalDateTime timestamp, double temperature, double humidity) {
    this.sensorId = sensorId;
    this.timestamp = timestamp;
    this.temperature = temperature;
    this.humidity = humidity;
  }

  public Long getSensorId() { return sensorId; }

  public LocalDateTime getTimestamp() { return timestamp; }

  public double getTemperature() { return temperature; }

  public double getHumidity() { return humidity; }
}
