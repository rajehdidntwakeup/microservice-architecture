package auth;

import com.example.microservice.architecture.sensorservice.SensorServiceApplication;
import com.example.microservice.architecture.sensorservice.auth.dto.AuthRequest;
import com.example.microservice.architecture.sensorservice.auth.dto.AuthResponse;
import com.example.microservice.architecture.sensorservice.auth.service.AuthenticationService;
import com.example.microservice.architecture.sensorservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SensorServiceApplication.class)
@ActiveProfiles("test")
@Transactional
public class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void register_ShouldCreateUserAndReturnToken() {
        AuthRequest request = new AuthRequest("testuser", "password");
        AuthResponse response = authenticationService.register(request);

        assertNotNull(response.getToken());
        assertFalse(response.isAdmin());
        assertTrue(userRepository.findByUsername("testuser").isPresent());
    }

    @Test
    void authenticate_ShouldReturnToken_WhenCredentialsAreValid() {
        AuthRequest registerRequest = new AuthRequest("testuser", "password");
        authenticationService.register(registerRequest);

        AuthRequest authRequest = new AuthRequest("testuser", "password");
        AuthResponse response = authenticationService.authenticate(authRequest);

        assertNotNull(response.getToken());
        assertFalse(response.isAdmin());
    }

    @Test
    void authenticate_ShouldThrowException_WhenCredentialsAreInvalid() {
        AuthRequest registerRequest = new AuthRequest("testuser", "password");
        authenticationService.register(registerRequest);

        AuthRequest authRequest = new AuthRequest("testuser", "wrongpassword");

        assertThrows(BadCredentialsException.class, () -> {
            authenticationService.authenticate(authRequest);
        });
    }

    @Test
    void authenticate_ShouldReturnAdminTrue_WhenUserHasAdminRole() {
        AuthRequest registerRequest = new AuthRequest("adminuser", "password");
        authenticationService.register(registerRequest);

        // Manually update role to ROLE_WRITE (admin)
        var user = userRepository.findByUsername("adminuser").orElseThrow();
        user.setRole("ROLE_WRITE");
        userRepository.save(user);

        AuthRequest authRequest = new AuthRequest("adminuser", "password");
        AuthResponse response = authenticationService.authenticate(authRequest);

        assertNotNull(response.getToken());
        assertTrue(response.isAdmin());
    }
}
