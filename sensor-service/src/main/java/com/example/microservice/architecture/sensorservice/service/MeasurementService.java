package com.example.microservice.architecture.sensorservice.service;


import com.example.microservice.architecture.sensorservice.dto.request.MeasurementRequestDto;
import com.example.microservice.architecture.sensorservice.dto.response.MeasurementResponseDto;
import com.example.microservice.architecture.sensorservice.entity.Measurement;
import com.example.microservice.architecture.sensorservice.entity.Sensor;
import com.example.microservice.architecture.sensorservice.exception.SensorNotFoundException;
import com.example.microservice.architecture.sensorservice.repository.MeasurementRepository;
import com.example.microservice.architecture.sensorservice.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MeasurementService {

  @Autowired
  private MeasurementRepository measurementRepository;
  @Autowired
  private SensorRepository sensorRepository;

  public List<MeasurementResponseDto> findAllMeasurements() {
    List<Measurement> measurements = measurementRepository.findAll();
    return measurements.stream()
        .map(this::convertToDto)
        .toList();
  }

  public MeasurementResponseDto findMeasurementById(Long id) {
    Optional<Measurement> measurement = measurementRepository.findById(id);
    return measurement.map(this::convertToDto).orElse(null);
  }

  public MeasurementResponseDto saveMeasurement(MeasurementRequestDto requestDto) {
    Optional<Sensor> sensor = sensorRepository.findById(requestDto.getSensorId());
    if (sensor.isEmpty()) {
      throw new SensorNotFoundException(requestDto.getSensorId());
    }
    Measurement measurement =
        measurementRepository.save(
            new Measurement(sensor.get(), requestDto.getTimestamp(),
                requestDto.getTemperature(), requestDto.getHumidity()));
    return convertToDto(measurement);
  }

  public List<MeasurementResponseDto> findMeasurementsBySensorId(Long sensorId) {
    Optional<Sensor> sensor = sensorRepository.findById(sensorId);
    if (sensor.isEmpty()) {
      throw new SensorNotFoundException(sensorId);
    }
    List<Measurement> measurements = measurementRepository.findBySensorId(sensorId);
    return measurements.stream().map(this::convertToDto).toList();
  }

  public void deleteMeasurement(Long id) {
    measurementRepository.deleteById(id);
  }

  private MeasurementResponseDto convertToDto(Measurement measurement) {
    return new MeasurementResponseDto(measurement.getId(), measurement.getSensor().getId(), measurement.getTimestamp(),
        measurement.getTemperature(), measurement.getHumidity());
  }
}