package controller;

import com.example.microservice.architecture.sensorservice.SensorServiceApplication;
import com.example.microservice.architecture.sensorservice.auth.dto.AuthRequest;
import com.example.microservice.architecture.sensorservice.auth.dto.AuthResponse;
import com.example.microservice.architecture.sensorservice.auth.service.AuthenticationService;
import com.example.microservice.architecture.sensorservice.entity.Measurement;
import com.example.microservice.architecture.sensorservice.entity.Sensor;
import com.example.microservice.architecture.sensorservice.repository.MeasurementRepository;
import com.example.microservice.architecture.sensorservice.repository.SensorRepository;
import com.example.microservice.architecture.sensorservice.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = SensorServiceApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MeasurementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private String authToken;
    private Sensor testSensor;

    @BeforeEach
    void setUp() {
        measurementRepository.deleteAll();
        sensorRepository.deleteAll();
        userRepository.deleteAll();

        // Create authenticated user with ROLE_WRITE
        AuthRequest request = new AuthRequest("testuser", "password");
        authenticationService.register(request);

        // Manually update role to WRITE as register defaults to READ
        com.example.microservice.architecture.sensorservice.entity.User user = userRepository.findByUsername("testuser").orElseThrow();
        user.setRole("ROLE_WRITE");
        userRepository.save(user);

        // Authenticate to get token
        AuthResponse response = authenticationService.authenticate(request);
        authToken = response.getToken();

        // Create test sensor
        testSensor = new Sensor("Test Sensor", "Test Location", true, "Temperature");
        testSensor = sensorRepository.save(testSensor);
    }

    @Test
    void testGetAllMeasurements_whenNoMeasurements_returnsNoContent() throws Exception {
        mockMvc.perform(get("/measurements")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAllMeasurements_whenMeasurementsExist_returnsOkWithList() throws Exception {
        // Create test measurements
        Measurement measurement1 = new Measurement();
        measurement1.setSensor(testSensor);
        measurement1.setTimestamp(Instant.now());
        measurement1.setTemperature(22.5);
        measurement1.setHumidity(65.0);
        measurementRepository.save(measurement1);

        Measurement measurement2 = new Measurement();
        measurement2.setSensor(testSensor);
        measurement2.setTimestamp(Instant.now().plusSeconds(3600));
        measurement2.setTemperature(23.0);
        measurement2.setHumidity(60.0);
        measurementRepository.save(measurement2);

        mockMvc.perform(get("/measurements")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].sensorId", is(testSensor.getId().intValue())))
                .andExpect(jsonPath("$[0].temperature", is(22.5)))
                .andExpect(jsonPath("$[0].humidity", is(65.0)));
    }

    @Test
    void testGetMeasurementById_whenMeasurementExists_returnsOk() throws Exception {
        Measurement measurement = new Measurement();
        measurement.setSensor(testSensor);
        measurement.setTimestamp(Instant.now());
        measurement.setTemperature(25.0);
        measurement.setHumidity(70.0);
        measurement = measurementRepository.save(measurement);

        mockMvc.perform(get("/measurements/" + measurement.getId())
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(measurement.getId().intValue())))
                .andExpect(jsonPath("$.sensorId", is(testSensor.getId().intValue())))
                .andExpect(jsonPath("$.temperature", is(25.0)))
                .andExpect(jsonPath("$.humidity", is(70.0)));
    }

    @Test
    void testGetMeasurementById_whenMeasurementNotFound_returnsNotFound() throws Exception {
        mockMvc.perform(get("/measurements/999")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateMeasurement_withValidData_returnsOk() throws Exception {
        Instant timestamp = Instant.now();
        String requestBody = String.format(
                "{\"sensorId\": %d, \"timestamp\": \"%s\", \"temperature\": 26.5, \"humidity\": 55.0}",
                testSensor.getId(), timestamp.toString()
        );

        mockMvc.perform(post("/measurements")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sensorId", is(testSensor.getId().intValue())))
                .andExpect(jsonPath("$.temperature", is(26.5)))
                .andExpect(jsonPath("$.humidity", is(55.0)))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    void testCreateMeasurement_withInvalidSensorId_returnsNotFound() throws Exception {
        Instant timestamp = Instant.now();
        String requestBody = String.format(
                "{\"sensorId\": 999, \"timestamp\": \"%s\", \"temperature\": 26.5, \"humidity\": 55.0}",
                timestamp.toString()
        );

        mockMvc.perform(post("/measurements")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetMeasurementsBySensorId_whenMeasurementsExist_returnsOk() throws Exception {
        // Create measurements for the test sensor
        Measurement measurement1 = new Measurement();
        measurement1.setSensor(testSensor);
        measurement1.setTimestamp(Instant.now());
        measurement1.setTemperature(20.0);
        measurement1.setHumidity(50.0);
        measurementRepository.save(measurement1);

        Measurement measurement2 = new Measurement();
        measurement2.setSensor(testSensor);
        measurement2.setTimestamp(Instant.now().plusSeconds(1800));
        measurement2.setTemperature(21.0);
        measurement2.setHumidity(52.0);
        measurementRepository.save(measurement2);

        mockMvc.perform(get("/measurements/sensor/" + testSensor.getId())
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].sensorId", is(testSensor.getId().intValue())))
                .andExpect(jsonPath("$[1].sensorId", is(testSensor.getId().intValue())));
    }

    @Test
    void testGetMeasurementsBySensorId_whenSensorNotFound_returnsNotFound() throws Exception {
        mockMvc.perform(get("/measurements/sensor/999")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteMeasurement_whenMeasurementExists_returnsOk() throws Exception {
        Measurement measurement = new Measurement();
        measurement.setSensor(testSensor);
        measurement.setTimestamp(Instant.now());
        measurement.setTemperature(24.0);
        measurement.setHumidity(68.0);
        measurement = measurementRepository.save(measurement);

        mockMvc.perform(delete("/measurements/" + measurement.getId())
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("deleted successfully")));
    }

    @Test
    void testMeasurementEndpoints_withoutAuthentication_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/measurements")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
