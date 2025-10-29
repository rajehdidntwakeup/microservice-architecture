package com.example.microservice.architecture.sensorservice.controller;

import java.util.List;

import com.example.microservice.architecture.sensorservice.dto.response.UserResponseDto;
import com.example.microservice.architecture.sensorservice.exception.UserNotFoundException;
import com.example.microservice.architecture.sensorservice.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;


  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<List<UserResponseDto>> getAllUsers() {
    List<UserResponseDto> allUsers = userService.findAllUsers();
    if (allUsers.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(allUsers);
  }

  @PutMapping("/{userId}/role/{role}")
  public ResponseEntity<UserResponseDto> updateUserRole(@Parameter(name = "userId") @PathVariable("userId") Long userId,
                                                        @Parameter(name = "role") @PathVariable("role") String role) {
    try {
      UserResponseDto updatedUser = userService.updateUserRole(userId, role);
      return ResponseEntity.ok(updatedUser);
    } catch (UserNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
  }
}
