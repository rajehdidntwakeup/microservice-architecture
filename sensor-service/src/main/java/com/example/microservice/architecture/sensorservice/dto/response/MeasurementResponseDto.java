package com.example.microservice.architecture.sensorservice.dto.response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MeasurementResponseDto {
  private Long id;
  private Long sensorId;
  private String timestamp;
  private double reading;
  private String unit;

  public MeasurementResponseDto() {}

  public MeasurementResponseDto(Long id, Long sensorId, LocalDateTime timestamp, double reading, String unit) {
    this.id = id;
    this.sensorId = sensorId;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    this.timestamp = timestamp.format(formatter);
    this.reading = reading;
    this.unit = unit;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public Long getSensorId() { return sensorId; }
  public void setSensorId(Long sensorId) { this.sensorId = sensorId; }

  public String getTimestamp() { return timestamp; }
  public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

  public double getReading() { return reading; }
  public void setReading(double reading) { this.reading = reading; }

  public String getUnit() { return unit; }
  public void setUnit(String unit) { this.unit = unit; }
}
