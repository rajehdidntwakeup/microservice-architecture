package com.example.microservice.architecture.sensorservice.auth.controller;


import com.example.microservice.architecture.sensorservice.auth.dto.AuthRequest;
import com.example.microservice.architecture.sensorservice.auth.dto.AuthResponse;
import com.example.microservice.architecture.sensorservice.auth.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  public AuthenticationController(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(
      @RequestBody AuthRequest request
  ) {
    return ResponseEntity.ok(authenticationService.register(request));
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthResponse> authenticate(
      @RequestBody AuthRequest request
  ) {
    return ResponseEntity.ok(authenticationService.authenticate(request));
  }
}

