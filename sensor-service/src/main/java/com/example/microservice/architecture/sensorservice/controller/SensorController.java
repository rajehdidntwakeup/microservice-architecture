package com.example.microservice.architecture.sensorservice.controller;


import com.example.microservice.architecture.sensorservice.dto.SensorDTO;
import com.example.microservice.architecture.sensorservice.entity.Sensor;
import com.example.microservice.architecture.sensorservice.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sensors")
public class SensorController {

  private final SensorService sensorService;

  public SensorController(SensorService sensorService) {
    this.sensorService = sensorService;
  }

  @GetMapping
  public List<SensorDTO> getAllSensors() {
    return sensorService.findAllSensors().stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  @GetMapping("/{id}")
  public ResponseEntity<SensorDTO> getSensorById(@PathVariable Long id) {
    return sensorService.findSensorById(id)
        .map(this::convertToDto)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public SensorDTO createSensor(@RequestBody SensorDTO sensorDTO) {
    Sensor sensor = convertToEntity(sensorDTO);
    return convertToDto(sensorService.saveSensor(sensor));
  }

  @PutMapping("/{id}")
  public ResponseEntity<SensorDTO> updateSensor(@PathVariable Long id, @RequestBody SensorDTO sensorDTO) {
    return sensorService.findSensorById(id)
        .map(existingSensor -> {
          existingSensor.setName(sensorDTO.getName());
          existingSensor.setLocation(sensorDTO.getLocation());
          existingSensor.setActive(sensorDTO.isActive());
          existingSensor.setType(sensorDTO.getType());
          return ResponseEntity.ok(convertToDto(sensorService.saveSensor(existingSensor)));
        })
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteSensor(@PathVariable Long id) {
    sensorService.deleteSensor(id);
    return ResponseEntity.noContent().build();
  }

  private SensorDTO convertToDto(Sensor sensor) {
    return new SensorDTO(sensor.getId(), sensor.getName(), sensor.getLocation(), sensor.isActive(), sensor.getType());
  }

  private Sensor convertToEntity(SensorDTO sensorDTO) {
    return new Sensor(sensorDTO.getId(), sensorDTO.getName(), sensorDTO.getLocation(), sensorDTO.isActive(), sensorDTO.getType());
  }
}
