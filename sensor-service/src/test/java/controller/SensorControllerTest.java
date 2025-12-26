package controller;

import com.example.microservice.architecture.sensorservice.SensorServiceApplication;
import com.example.microservice.architecture.sensorservice.dto.request.SensorRequestDto;
import com.example.microservice.architecture.sensorservice.entity.Sensor;
import com.example.microservice.architecture.sensorservice.entity.User;
import com.example.microservice.architecture.sensorservice.repository.MeasurementRepository;
import com.example.microservice.architecture.sensorservice.repository.SensorRepository;
import com.example.microservice.architecture.sensorservice.repository.UserRepository;
import com.example.microservice.architecture.sensorservice.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = SensorServiceApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SensorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private String writeToken;
    private String readToken;

    @BeforeEach
    void setUp() {
        measurementRepository.deleteAll();
        sensorRepository.deleteAll();
        userRepository.deleteAll();

        User writeUser = new User();
        writeUser.setUsername("writeuser");
        writeUser.setPassword(passwordEncoder.encode("password"));
        writeUser.setRole("ROLE_WRITE");
        userRepository.save(writeUser);
        writeToken = jwtService.generateToken(writeUser);

        User readUser = new User();
        readUser.setUsername("readuser");
        readUser.setPassword(passwordEncoder.encode("password"));
        readUser.setRole("ROLE_READ");
        userRepository.save(readUser);
        readToken = jwtService.generateToken(readUser);
    }

    @Test
    void testGetAllSensors_Empty() throws Exception {
        mockMvc.perform(get("/sensors")
                        .header("Authorization", "Bearer " + readToken))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAllSensors_NotEmpty() throws Exception {
        sensorRepository.save(new Sensor("Sensor 1", "Room 1", true, "Temperature"));
        sensorRepository.save(new Sensor("Sensor 2", "Room 2", true, "Humidity"));

        mockMvc.perform(get("/sensors")
                        .header("Authorization", "Bearer " + readToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Sensor 1")))
                .andExpect(jsonPath("$[1].name", is("Sensor 2")));
    }

    @Test
    void testGetSensorById_Found() throws Exception {
        Sensor sensor = sensorRepository.save(new Sensor("Sensor 1", "Room 1", true, "Temperature"));

        mockMvc.perform(get("/sensors/" + sensor.getId())
                        .header("Authorization", "Bearer " + readToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Sensor 1")))
                .andExpect(jsonPath("$.location", is("Room 1")));
    }

    @Test
    void testGetSensorById_NotFound() throws Exception {
        mockMvc.perform(get("/sensors/999")
                        .header("Authorization", "Bearer " + readToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateSensor() throws Exception {
        SensorRequestDto requestDto = new SensorRequestDto("New Sensor", "Lab", true, "CO2");

        mockMvc.perform(post("/sensors")
                        .header("Authorization", "Bearer " + writeToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("New Sensor")))
                .andExpect(jsonPath("$.location", is("Lab")));
    }

    @Test
    void testCreateSensor_Forbidden() throws Exception {
        SensorRequestDto requestDto = new SensorRequestDto("New Sensor", "Lab", true, "CO2");

        mockMvc.perform(post("/sensors")
                        .header("Authorization", "Bearer " + readToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdateSensor_Found() throws Exception {
        Sensor sensor = sensorRepository.save(new Sensor("Old Name", "Old Location", true, "Type"));
        SensorRequestDto updateDto = new SensorRequestDto("New Name", "New Location", false, "New Type");

        mockMvc.perform(put("/sensors/" + sensor.getId())
                        .header("Authorization", "Bearer " + writeToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("New Name")))
                .andExpect(jsonPath("$.location", is("New Location")))
                .andExpect(jsonPath("$.active", is(false)));
    }

    @Test
    void testUpdateSensor_NotFound() throws Exception {
        SensorRequestDto updateDto = new SensorRequestDto("New Name", "New Location", false, "New Type");

        mockMvc.perform(put("/sensors/999")
                        .header("Authorization", "Bearer " + writeToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteSensor() throws Exception {
        Sensor sensor = sensorRepository.save(new Sensor("To Delete", "Somewhere", true, "Type"));

        mockMvc.perform(delete("/sensors/" + sensor.getId())
                        .header("Authorization", "Bearer " + writeToken))
                .andExpect(status().isOk())
                .andExpect(content().string("Sensor with id " + sensor.getId() + " deleted successfully"));

        mockMvc.perform(get("/sensors/" + sensor.getId())
                        .header("Authorization", "Bearer " + readToken))
                .andExpect(status().isNotFound());
    }
}
