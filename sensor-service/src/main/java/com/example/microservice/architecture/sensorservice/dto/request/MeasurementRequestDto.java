package com.example.microservice.architecture.sensorservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MeasurementRequestDto {
  private Long sensorId;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime timestamp;
  private double temperature;
  private double humidity;

  public MeasurementRequestDto() {}

  public MeasurementRequestDto(Long sensorId, String timestamp, double temperature, double humidity) {
    this.sensorId = sensorId;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    this.timestamp = LocalDateTime.parse(timestamp, formatter);
    this.temperature = temperature;
    this.humidity = humidity;
  }

  public Long getSensorId() { return sensorId; }

  public LocalDateTime getTimestamp() { return timestamp; }

  public double getTemperature() {
    return temperature;
  }

  public double getHumidity() {
    return humidity;
  }
}
