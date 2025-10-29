package com.example.microservice.architecture.sensorservice.controller;


import java.util.List;

import com.example.microservice.architecture.sensorservice.dto.request.SensorRequestDto;
import com.example.microservice.architecture.sensorservice.dto.response.SensorResponseDto;
import com.example.microservice.architecture.sensorservice.exception.SensorNotFoundException;
import com.example.microservice.architecture.sensorservice.service.SensorService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/sensors")
public class SensorController {

  private final SensorService sensorService;

  public SensorController(SensorService sensorService) {
    this.sensorService = sensorService;
  }

  @GetMapping
  public ResponseEntity<List<SensorResponseDto>> getAllSensors() {
    List<SensorResponseDto> responseDtoList = sensorService.findAllSensors();
    if (!responseDtoList.isEmpty()) {
      return ResponseEntity.ok(responseDtoList);
    } else {
      return ResponseEntity.noContent().build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<SensorResponseDto> getSensorById(@Parameter(name = "id", description = "Sensor ID") @PathVariable("id") Long id) {
    SensorResponseDto sensorResponseDto = sensorService.findSensorById(id);
    if (sensorResponseDto != null) {
      return ResponseEntity.ok(sensorResponseDto);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping
  public ResponseEntity<SensorResponseDto> createSensor(@RequestBody SensorRequestDto sensorRequestDto) {
    SensorResponseDto sensorResponseDto = sensorService.saveSensor(sensorRequestDto);
    return ResponseEntity.ok(sensorResponseDto);
  }

  @PutMapping("/{id}")
  public ResponseEntity<SensorResponseDto> updateSensor(@Parameter(name = "id", description = "Sensor ID") @PathVariable("id") Long id, @RequestBody
  SensorRequestDto sensorRequestDto) {
    try {
      SensorResponseDto sensorResponseDto = sensorService.updateSensor(id, sensorRequestDto);
      return ResponseEntity.ok(sensorResponseDto);
    } catch (SensorNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteSensor(@Parameter(name = "id", description = "Sensor ID") @PathVariable("id") Long id) {
    sensorService.deleteSensor(id);
    return ResponseEntity.ok("Sensor with id " + id + " deleted successfully");
  }
}
