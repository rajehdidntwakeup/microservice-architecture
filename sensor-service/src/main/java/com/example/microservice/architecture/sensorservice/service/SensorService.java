package com.example.microservice.architecture.sensorservice.service;

import com.example.microservice.architecture.sensorservice.entity.Sensor;
import com.example.microservice.architecture.sensorservice.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SensorService {

  @Autowired
  private SensorRepository sensorRepository;

  public List<Sensor> findAllSensors() {
    return sensorRepository.findAll();
  }

  public Optional<Sensor> findSensorById(Long id) {
    return sensorRepository.findById(id);
  }

  public Sensor saveSensor(Sensor sensor) {
    return sensorRepository.save(sensor);
  }

  public void deleteSensor(Long id) {
    sensorRepository.deleteById(id);
  }
}
