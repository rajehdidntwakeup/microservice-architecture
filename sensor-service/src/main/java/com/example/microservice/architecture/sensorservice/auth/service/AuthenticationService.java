package com.example.microservice.architecture.sensorservice.auth.service;

import com.example.microservice.architecture.sensorservice.auth.dto.AuthRequest;
import com.example.microservice.architecture.sensorservice.auth.dto.AuthResponse;
import com.example.microservice.architecture.sensorservice.entity.User;
import com.example.microservice.architecture.sensorservice.repository.UserRepository;
import com.example.microservice.architecture.sensorservice.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationService(UserRepository userRepository,
                               PasswordEncoder passwordEncoder,
                               JwtService jwtService,
                               AuthenticationManager authenticationManager) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    this.authenticationManager = authenticationManager;
  }

  public AuthResponse register(AuthRequest request) {
    User user = new User();
    user.setUsername(request.getUsername());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole("ROLE_READ"); // Default role for new users
    userRepository.save(user);
    var jwtToken = jwtService.generateToken(user);
    return new AuthResponse(jwtToken, false);
  }

  public AuthResponse authenticate(AuthRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getUsername(),
            request.getPassword()
        )
    );
    var user = userRepository.findByUsername(request.getUsername())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    boolean isAdmin = user.getRole().equals("ROLE_WRITE");
    return new AuthResponse(jwtToken, isAdmin);
  }


}
