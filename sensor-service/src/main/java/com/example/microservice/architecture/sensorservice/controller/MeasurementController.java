package com.example.microservice.architecture.sensorservice.controller;

import com.example.microservice.architecture.sensorservice.dto.MeasurementDTO;
import com.example.microservice.architecture.sensorservice.entity.Measurement;
import com.example.microservice.architecture.sensorservice.entity.Sensor;
import com.example.microservice.architecture.sensorservice.service.MeasurementService;
import com.example.microservice.architecture.sensorservice.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {

  private final MeasurementService measurementService;
  private final SensorService sensorService;

  public MeasurementController(MeasurementService measurementService, SensorService sensorService) {
    this.measurementService = measurementService;
    this.sensorService = sensorService;
  }

  @GetMapping
  public List<MeasurementDTO> getAllMeasurements() {
    return measurementService.findAllMeasurements().stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  @GetMapping("/{id}")
  public ResponseEntity<MeasurementDTO> getMeasurementById(@PathVariable Long id) {
    return measurementService.findMeasurementById(id)
        .map(this::convertToDto)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<MeasurementDTO> createMeasurement(@RequestBody MeasurementDTO measurementDTO) {
    return sensorService.findSensorById(measurementDTO.getSensorId())
        .map(sensor -> {
          Measurement measurement = convertToEntity(measurementDTO, sensor);
          return ResponseEntity.ok(convertToDto(measurementService.saveMeasurement(measurement)));
        })
        .orElse(ResponseEntity.badRequest().build()); // Sensor not found
  }

  @GetMapping("/sensor/{sensorId}")
  public List<MeasurementDTO> getMeasurementsBySensorId(@PathVariable Long sensorId) {
    return measurementService.findMeasurementsBySensorId(sensorId).stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteMeasurement(@PathVariable Long id) {
    measurementService.deleteMeasurement(id);
    return ResponseEntity.noContent().build();
  }

  private MeasurementDTO convertToDto(Measurement measurement) {
    return new MeasurementDTO(measurement.getId(), measurement.getSensor().getId(), measurement.getTimestamp(), measurement.getTemperature(), measurement.getHumidity());
  }

  private Measurement convertToEntity(MeasurementDTO measurementDTO, Sensor sensor) {
    return new Measurement(measurementDTO.getId(), sensor, measurementDTO.getTimestamp(), measurementDTO.getTemperature(), measurementDTO.getHumidity());
  }
}
