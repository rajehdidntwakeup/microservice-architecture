package com.example.microservice.architecture.sensorservice.auth.dto;

public class AuthResponse {
  private String token;
  private boolean isAdmin;

  public AuthResponse() {
  }

    public AuthResponse(String token, boolean isAdmin) {
        this.token = token;
        this.isAdmin = isAdmin;
    }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public boolean isAdmin() {
    return isAdmin;
  }

  public void setAdmin(boolean admin) {
    isAdmin = admin;
  }
}
