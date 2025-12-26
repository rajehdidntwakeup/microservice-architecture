package service;
import com.example.microservice.architecture.sensorservice.SensorServiceApplication;
import com.example.microservice.architecture.sensorservice.dto.request.MeasurementRequestDto;
import com.example.microservice.architecture.sensorservice.dto.response.MeasurementResponseDto;
import com.example.microservice.architecture.sensorservice.entity.Measurement;
import com.example.microservice.architecture.sensorservice.entity.Sensor;
import com.example.microservice.architecture.sensorservice.exception.SensorNotFoundException;
import com.example.microservice.architecture.sensorservice.repository.MeasurementRepository;
import com.example.microservice.architecture.sensorservice.repository.SensorRepository;
import com.example.microservice.architecture.sensorservice.service.MeasurementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SensorServiceApplication.class)
@ActiveProfiles("test")
@Transactional
public class MeasurementServiceTest {

    @Autowired
    private MeasurementService measurementService;

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private SensorRepository sensorRepository;

    private Sensor testSensor;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @BeforeEach
    void setUp() {
        measurementRepository.deleteAll();
        sensorRepository.deleteAll();

        testSensor = new Sensor("Test Sensor", "Test Location", true, "Temperature");
        testSensor = sensorRepository.save(testSensor);
    }

    @Test
    void testFindAllMeasurements_whenNoMeasurements_returnsEmptyList() {
        List<MeasurementResponseDto> result = measurementService.findAllMeasurements();
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAllMeasurements_whenMeasurementsExist_returnsList() {
        Measurement measurement = new Measurement(testSensor, LocalDateTime.now(), 25.0, 60.0);
        measurementRepository.save(measurement);

        List<MeasurementResponseDto> result = measurementService.findAllMeasurements();
        assertEquals(1, result.size());
        assertEquals(testSensor.getId(), result.get(0).getSensorId());
    }

    @Test
    void testFindMeasurementById_whenExists_returnsDto() {
        Measurement measurement = new Measurement(testSensor, LocalDateTime.now(), 25.0, 60.0);
        measurement = measurementRepository.save(measurement);

        MeasurementResponseDto result = measurementService.findMeasurementById(measurement.getId());
        assertNotNull(result);
        assertEquals(measurement.getId(), result.getId());
    }

    @Test
    void testFindMeasurementById_whenNotFound_returnsNull() {
        MeasurementResponseDto result = measurementService.findMeasurementById(999L);
        assertNull(result);
    }

    @Test
    void testSaveMeasurement_withValidData_returnsSavedDto() {
        String timestampStr = LocalDateTime.now().format(formatter);
        MeasurementRequestDto requestDto = new MeasurementRequestDto(testSensor.getId(), timestampStr, 22.5, 55.0);

        MeasurementResponseDto result = measurementService.saveMeasurement(requestDto);

        assertNotNull(result.getId());
        assertEquals(testSensor.getId(), result.getSensorId());
        assertEquals(22.5, result.getTemperature());
        assertEquals(55.0, result.getHumidity());
    }

    @Test
    void testSaveMeasurement_withInvalidSensorId_throwsException() {
        MeasurementRequestDto requestDto = new MeasurementRequestDto(999L, LocalDateTime.now().format(formatter), 22.5, 55.0);

        assertThrows(SensorNotFoundException.class, () -> measurementService.saveMeasurement(requestDto));
    }

    @Test
    void testFindMeasurementsBySensorId_whenExists_returnsList() {
        Measurement measurement = new Measurement(testSensor, LocalDateTime.now(), 25.0, 60.0);
        measurementRepository.save(measurement);

        List<MeasurementResponseDto> result = measurementService.findMeasurementsBySensorId(testSensor.getId());
        assertEquals(1, result.size());
        assertEquals(testSensor.getId(), result.get(0).getSensorId());
    }

    @Test
    void testFindMeasurementsBySensorId_whenSensorNotFound_throwsException() {
        assertThrows(SensorNotFoundException.class, () -> measurementService.findMeasurementsBySensorId(999L));
    }

    @Test
    void testDeleteMeasurement_whenExists_removesMeasurement() {
        Measurement measurement = new Measurement(testSensor, LocalDateTime.now(), 25.0, 60.0);
        measurement = measurementRepository.save(measurement);
        Long id = measurement.getId();

        measurementService.deleteMeasurement(id);

        assertFalse(measurementRepository.findById(id).isPresent());
    }
}
