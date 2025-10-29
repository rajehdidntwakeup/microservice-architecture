package com.example.microservice.architecture.sensorservice.dto.response;

public class SensorResponseDto {
  private Long id;
  private String name;
  private String location;
  private boolean active;
  private String type;

  public SensorResponseDto() {}

  public SensorResponseDto(Long id, String name, String location, boolean active, String type) {
    this.id = id;
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
}
