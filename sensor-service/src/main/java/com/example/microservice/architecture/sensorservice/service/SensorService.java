package com.example.microservice.architecture.sensorservice.service;

import java.util.List;
import java.util.Optional;

import com.example.microservice.architecture.sensorservice.dto.request.SensorRequestDto;
import com.example.microservice.architecture.sensorservice.dto.response.SensorResponseDto;
import com.example.microservice.architecture.sensorservice.entity.Sensor;
import com.example.microservice.architecture.sensorservice.exception.SensorNotFoundException;
import com.example.microservice.architecture.sensorservice.repository.SensorRepository;
import org.springframework.stereotype.Service;

@Service
public class SensorService {

  private final SensorRepository sensorRepository;

  public SensorService(SensorRepository sensorRepository) {
    this.sensorRepository = sensorRepository;
  }

  public List<SensorResponseDto> findAllSensors() {
    List<Sensor> sensors = sensorRepository.findAll();
    return sensors.stream().map(this::convertToDto).toList();
  }

  public SensorResponseDto findSensorById(Long id) {
    Optional<Sensor> sensor = sensorRepository.findById(id);
    return sensor.map(this::convertToDto).orElse(null);
  }

  public SensorResponseDto saveSensor(SensorRequestDto sensorRequestDto) {
    Sensor sensor = sensorRepository.save(new Sensor(sensorRequestDto.getName(),
        sensorRequestDto.getLocation(),
        sensorRequestDto.isActive(),
        sensorRequestDto.getType()));
    return convertToDto(sensor);
  }

  public SensorResponseDto updateSensor(Long id, SensorRequestDto sensorRequestDto) {
    Optional<Sensor> sensor = sensorRepository.findById(id);
    if (sensor.isEmpty()) {
      throw new SensorNotFoundException(id);
    }
    Sensor updatedSensor = sensor.get();
    updatedSensor.setName(sensorRequestDto.getName());
    updatedSensor.setLocation(sensorRequestDto.getLocation());
    updatedSensor.setActive(sensorRequestDto.isActive());
    updatedSensor.setType(sensorRequestDto.getType());
    return convertToDto(sensorRepository.save(updatedSensor));
  }

  public void deleteSensor(Long id) {
    sensorRepository.deleteById(id);
  }

  private SensorResponseDto convertToDto(Sensor sensor) {
    return new SensorResponseDto(sensor.getId(), sensor.getName(), sensor.getLocation(), sensor.isActive(),
        sensor.getType());
  }
}
