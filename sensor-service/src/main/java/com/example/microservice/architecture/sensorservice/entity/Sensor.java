package com.example.microservice.architecture.sensorservice.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Sensor {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String location;
  private boolean active;
  private String type;
  @OneToMany(mappedBy = "sensor", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
  private List<Measurement> measurements = new ArrayList<>();

  public Sensor() {}

  public Sensor(String name, String location, boolean active, String type) {
    this.name = name;
    this.location = location;
    this.active = active;
    this.type = type;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getLocation() { return location; }
  public void setLocation(String location) { this.location = location; }

  public boolean isActive() { return active; }
  public void setActive(boolean active) { this.active = active; }

  public String getType() { return type; }
  public void setType(String type) { this.type = type; }

  public List<Measurement> getMeasurements() {
    return measurements;
  }

  public void setMeasurements(
      List<Measurement> measurements) {
    this.measurements = measurements;
  }
}
