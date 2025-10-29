package com.example.microservice.architecture.sensorservice.dto.request;

public class SensorRequestDto {
  private String name;
  private String location;
  private boolean active;
  private String type;

  public SensorRequestDto() {}

  public SensorRequestDto(String name, String location, boolean active, String type) {
    this.name = name;
    this.location = location;
    this.active = active;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public String getLocation() {
    return location;
  }

  public boolean isActive() {
    return active;
  }

  public String getType() {
    return type;
  }
}
