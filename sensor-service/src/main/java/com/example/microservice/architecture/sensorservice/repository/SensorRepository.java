package com.example.microservice.architecture.sensorservice.repository;

import com.example.microservice.architecture.sensorservice.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {
}
