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
  private double reading;
  private String unit;

  public Measurement() {}

  public Measurement(Sensor sensor, LocalDateTime timestamp, double reading, String unit) {
    this.sensor = sensor;
    this.timestamp = timestamp;
    this.reading = reading;
    this.unit = unit;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public Sensor getSensor() { return sensor; }
  public void setSensor(Sensor sensor) { this.sensor = sensor; }

  public LocalDateTime getTimestamp() { return timestamp; }
  public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

  public double getReading() { return reading; }
  public void setReading(double reading) { this.reading = reading; }

  public String getUnit() { return unit; }
  public void setUnit(String unit) { this.unit = unit; }
}
