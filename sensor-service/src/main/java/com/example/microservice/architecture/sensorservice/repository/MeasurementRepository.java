package com.example.microservice.architecture.sensorservice.repository;

import com.example.microservice.architecture.sensorservice.entity.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
  List<Measurement> findBySensorId(Long sensorId);
}
