package com.example.microservice.architecture.sensorservice.service;

import java.util.List;
import java.util.Optional;

import com.example.microservice.architecture.sensorservice.dto.response.UserResponseDto;
import com.example.microservice.architecture.sensorservice.entity.User;
import com.example.microservice.architecture.sensorservice.exception.UserNotFoundException;
import com.example.microservice.architecture.sensorservice.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<UserResponseDto> findAllUsers() {
    return userRepository.findAll().stream()
        .map(u -> new UserResponseDto(u.getId(), u.getUsername(), u.getRole()))
        .toList();
  }

  public UserResponseDto updateUserRole(Long userId, String role) {
    Optional<User> user = userRepository.findById(userId);
    if (user.isEmpty()) {
      throw new UserNotFoundException(userId);
    }
    String normalizedRole = normalizeRole(role);
    if (normalizedRole == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "Invalid role. Allowed values: ROLE_READ, ROLE_WRITE (or READ, WRITE)");
    }
    User updatedUser = user.get();
    updatedUser.setRole(normalizedRole);
    userRepository.save(updatedUser);
    return new UserResponseDto(updatedUser.getId(), updatedUser.getUsername(), updatedUser.getRole());
  }

  public String deleteUserById(Long userId) {
    Optional<User> user = userRepository.findById(userId);
    if (user.isEmpty()) {
      throw new UserNotFoundException(userId);
    }
    userRepository.deleteById(userId);
    return "User with id " + userId + " deleted successfully";
  }

  private String normalizeRole(String role) {
    if (role == null) {
      return null;
    }
    String r = role.trim().toUpperCase();
    if (r.equals("ROLE_WRITE") || r.equals("WRITE")) {
      return "ROLE_WRITE";
    }
    if (r.equals("ROLE_READ") || r.equals("READ")) {
      return "ROLE_READ";
    }
    return null;
  }
}
