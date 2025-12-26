package service;
import com.example.microservice.architecture.sensorservice.SensorServiceApplication;
import com.example.microservice.architecture.sensorservice.dto.request.SensorRequestDto;
import com.example.microservice.architecture.sensorservice.dto.response.SensorResponseDto;
import com.example.microservice.architecture.sensorservice.entity.Sensor;
import com.example.microservice.architecture.sensorservice.exception.SensorNotFoundException;
import com.example.microservice.architecture.sensorservice.repository.SensorRepository;
import com.example.microservice.architecture.sensorservice.service.SensorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SensorServiceApplication.class)
@ActiveProfiles("test")
@Transactional
public class SensorServiceTest {

    @Autowired
    private SensorService sensorService;

    @Autowired
    private SensorRepository sensorRepository;

    @BeforeEach
    void setUp() {
        sensorRepository.deleteAll();
    }

    @Test
    void testFindAllSensors_whenNoSensors_returnsEmptyList() {
        List<SensorResponseDto> result = sensorService.findAllSensors();
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAllSensors_whenSensorsExist_returnsList() {
        sensorRepository.save(new Sensor("Sensor 1", "Location 1", true, "Temperature"));
        sensorRepository.save(new Sensor("Sensor 2", "Location 2", false, "Humidity"));

        List<SensorResponseDto> result = sensorService.findAllSensors();

        assertEquals(2, result.size());
    }

    @Test
    void testFindSensorById_whenExists_returnsDto() {
        Sensor sensor = sensorRepository.save(new Sensor("Sensor 1", "Location 1", true, "Temperature"));

        SensorResponseDto result = sensorService.findSensorById(sensor.getId());

        assertNotNull(result);
        assertEquals(sensor.getName(), result.getName());
    }

    @Test
    void testFindSensorById_whenNotFound_returnsNull() {
        SensorResponseDto result = sensorService.findSensorById(999L);
        assertNull(result);
    }

    @Test
    void testSaveSensor_withValidData_returnsSavedDto() {
        SensorRequestDto requestDto = new SensorRequestDto("New Sensor", "New Location", true, "CO2");

        SensorResponseDto result = sensorService.saveSensor(requestDto);

        assertNotNull(result.getId());
        assertEquals("New Sensor", result.getName());
        assertEquals("New Location", result.getLocation());
        assertTrue(result.isActive());
        assertEquals("CO2", result.getType());
    }

    @Test
    void testUpdateSensor_whenExists_updatesAndReturnsDto() {
        Sensor sensor = sensorRepository.save(new Sensor("Old Name", "Old Location", false, "Pressure"));
        SensorRequestDto updateDto = new SensorRequestDto("Updated Name", "Updated Location", true, "Temperature");

        SensorResponseDto result = sensorService.updateSensor(sensor.getId(), updateDto);

        assertEquals("Updated Name", result.getName());
        assertEquals("Updated Location", result.getLocation());
        assertTrue(result.isActive());
        assertEquals("Temperature", result.getType());

        Sensor updatedInDb = sensorRepository.findById(sensor.getId()).orElseThrow();
        assertEquals("Updated Name", updatedInDb.getName());
    }

    @Test
    void testUpdateSensor_whenNotFound_throwsException() {
        SensorRequestDto updateDto = new SensorRequestDto("Name", "Location", true, "Type");

        assertThrows(SensorNotFoundException.class, () -> sensorService.updateSensor(999L, updateDto));
    }

    @Test
    void testDeleteSensor_whenExists_removesSensor() {
        Sensor sensor = sensorRepository.save(new Sensor("To Delete", "Location", true, "Type"));
        Long id = sensor.getId();

        sensorService.deleteSensor(id);

        assertFalse(sensorRepository.findById(id).isPresent());
    }
}
