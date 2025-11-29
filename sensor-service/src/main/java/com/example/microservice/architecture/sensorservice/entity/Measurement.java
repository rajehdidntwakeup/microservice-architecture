package com.example.microservice.architecture.sensorservice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Use a lowercase, unquoted table name to avoid case-sensitivity issues on H2
// and stay consistent with Spring's physical naming strategy.
@Table(name = "measurement")
@Entity
public class Measurement {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "sensor_id")
  private Sensor sensor;

  private LocalDateTime timestamp;
  private double temperature;
  private double humidity;

  public Measurement() {}

  public Measurement(Sensor sensor, LocalDateTime timestamp, double temperature, double humidity) {
    this.sensor = sensor;
    this.timestamp = timestamp;
    this.temperature = temperature;
    this.humidity = humidity;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public Sensor getSensor() { return sensor; }
  public void setSensor(Sensor sensor) { this.sensor = sensor; }

  public LocalDateTime getTimestamp() { return timestamp; }
  public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

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
