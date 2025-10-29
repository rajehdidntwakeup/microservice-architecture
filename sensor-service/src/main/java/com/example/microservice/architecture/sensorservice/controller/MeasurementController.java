package com.example.microservice.architecture.sensorservice.controller;

import java.util.List;

import com.example.microservice.architecture.sensorservice.dto.request.MeasurementRequestDto;
import com.example.microservice.architecture.sensorservice.dto.response.MeasurementResponseDto;
import com.example.microservice.architecture.sensorservice.exception.SensorNotFoundException;
import com.example.microservice.architecture.sensorservice.service.MeasurementService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {

  private final MeasurementService measurementService;

  public MeasurementController(MeasurementService measurementService) {
    this.measurementService = measurementService;
  }

  @GetMapping
  public ResponseEntity<List<MeasurementResponseDto>> getAllMeasurements() {
    List<MeasurementResponseDto> responseDtoList = measurementService.findAllMeasurements();
    if (!responseDtoList.isEmpty()) {
      return ResponseEntity.ok(responseDtoList);
    } else {
      return ResponseEntity.noContent().build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<MeasurementResponseDto> getMeasurementById(
      @Parameter(name = "id", description = "Measurement ID") @PathVariable("id") Long id) {
    MeasurementResponseDto measurementResponseDto = measurementService.findMeasurementById(id);
    if (measurementResponseDto != null) {
      return ResponseEntity.ok(measurementResponseDto);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping
  public ResponseEntity<MeasurementResponseDto> createMeasurement(@RequestBody
                                                                  MeasurementRequestDto measurementRequestDto) {
    try {
      MeasurementResponseDto measurementResponseDto =
          measurementService.saveMeasurement(measurementRequestDto);
      return ResponseEntity.ok(measurementResponseDto);
    } catch (SensorNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
  }

  @GetMapping("/sensor/{sensorId}")
  public ResponseEntity<List<MeasurementResponseDto>> getMeasurementsBySensorId(
      @Parameter(name = "sensorId", description = "Sensor ID") @PathVariable("sensorId") Long sensorId) {
    try {
      List<MeasurementResponseDto> measurements = measurementService.findMeasurementsBySensorId(sensorId);
      return ResponseEntity.ok(measurements);
    } catch (SensorNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteMeasurement(
      @Parameter(name = "id", description = "Measurement ID") @PathVariable("id") Long id) {
    measurementService.deleteMeasurement(id);
    return ResponseEntity.ok("Measurement with id " + id + " deleted successfully");
  }
}
