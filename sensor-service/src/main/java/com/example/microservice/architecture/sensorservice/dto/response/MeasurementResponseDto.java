package com.example.microservice.architecture.sensorservice.dto.response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MeasurementResponseDto {
  private Long id;
  private Long sensorId;
  private String timestamp;
  private double temperature;
  private double humidity;

  public MeasurementResponseDto() {}

  public MeasurementResponseDto(Long id, Long sensorId, LocalDateTime timestamp, double temperature, double humidity) {
    this.id = id;
    this.sensorId = sensorId;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    this.timestamp = timestamp.format(formatter);
    this.temperature = temperature;
    this.humidity = humidity;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public Long getSensorId() { return sensorId; }
  public void setSensorId(Long sensorId) { this.sensorId = sensorId; }

  public String getTimestamp() { return timestamp; }
  public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

  public double getTemperature() {
    return temperature;
  }

  public void setTemperature(double temperature) {
    this.temperature = temperature;
  }

  public double getHumidity() {
    return humidity;
  }

  public void setHumidity(double humidity) {
    this.humidity = humidity;
  }
}
