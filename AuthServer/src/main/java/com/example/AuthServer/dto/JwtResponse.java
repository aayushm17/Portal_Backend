package com.example.AuthServer.dto;

public class JwtResponse {
    private boolean valid;

    // Constructors, getters, and setters
    public JwtResponse() {}

    public JwtResponse(boolean valid) {
        this.valid = valid;
    }

    // Getters and setters
    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}

