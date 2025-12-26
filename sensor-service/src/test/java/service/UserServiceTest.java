package service;

import com.example.microservice.architecture.sensorservice.SensorServiceApplication;
import com.example.microservice.architecture.sensorservice.dto.response.UserResponseDto;
import com.example.microservice.architecture.sensorservice.entity.User;
import com.example.microservice.architecture.sensorservice.exception.UserNotFoundException;
import com.example.microservice.architecture.sensorservice.repository.UserRepository;
import com.example.microservice.architecture.sensorservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = SensorServiceApplication.class)
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testFindAllUsers() {
        User user1 = new User(null, "user1", "pass1", "ROLE_READ");
        User user2 = new User(null, "user2", "pass2", "ROLE_WRITE");
        userRepository.saveAll(List.of(user1, user2));

        List<UserResponseDto> users = userService.findAllUsers();

        assertThat(users).hasSize(2);
        assertThat(users).extracting(UserResponseDto::getUsername).containsExactlyInAnyOrder("user1", "user2");
    }

    @Test
    void testUpdateUserRole_Success() {
        User user = new User(null, "user1", "pass1", "ROLE_READ");
        user = userRepository.save(user);

        UserResponseDto updatedUser = userService.updateUserRole(user.getId(), "WRITE");

        assertThat(updatedUser.getRole()).isEqualTo("ROLE_WRITE");
        User savedUser = userRepository.findById(user.getId()).orElseThrow();
        assertThat(savedUser.getRole()).isEqualTo("ROLE_WRITE");
    }

    @Test
    void testUpdateUserRole_InvalidRole() {
        User user = new User(null, "user1", "pass1", "ROLE_READ");
        user = userRepository.save(user);

        Long userId = user.getId();
        assertThrows(ResponseStatusException.class, () -> userService.updateUserRole(userId, "INVALID_ROLE"));
    }

    @Test
    void testUpdateUserRole_UserNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.updateUserRole(999L, "READ"));
    }

    @Test
    void testDeleteUserById_Success() {
        User user = new User(null, "user1", "pass1", "ROLE_READ");
        user = userRepository.save(user);

        String result = userService.deleteUserById(user.getId());

        assertThat(result).contains("deleted successfully");
        assertThat(userRepository.findById(user.getId())).isEmpty();
    }

    @Test
    void testDeleteUserById_UserNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(999L));
    }
}
