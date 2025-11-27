package com.example.microservice.architecture.sensorservice.dto.request;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MeasurementRequestDto {
  private Long sensorId;
  private LocalDateTime timestamp;
  private double reading;
  private String unit;

  public MeasurementRequestDto() {}

  public MeasurementRequestDto(Long sensorId, String timestamp, double reading, String unit) {
    this.sensorId = sensorId;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    this.timestamp = LocalDateTime.parse(timestamp, formatter);
    this.reading = reading;
    this.unit = unit;
  }

  public Long getSensorId() { return sensorId; }

  public LocalDateTime getTimestamp() { return timestamp; }

  public double getReading() {
    return reading;
  }

  public String getUnit() {
    return unit;
  }
}
