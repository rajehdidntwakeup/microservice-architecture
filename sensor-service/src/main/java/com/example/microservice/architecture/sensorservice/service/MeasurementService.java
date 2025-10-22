package com.example.microservice.architecture.sensorservice.service;


import com.example.microservice.architecture.sensorservice.entity.Measurement;
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

  public List<Measurement> findAllMeasurements() {
    return measurementRepository.findAll();
  }

  public Optional<Measurement> findMeasurementById(Long id) {
    return measurementRepository.findById(id);
  }

  public Measurement saveMeasurement(Measurement measurement) {
    return measurementRepository.save(measurement);
  }

  public List<Measurement> findMeasurementsBySensorId(Long sensorId) {
    return measurementRepository.findBySensorId(sensorId);
  }

  public void deleteMeasurement(Long id) {
    measurementRepository.deleteById(id);
  }
}