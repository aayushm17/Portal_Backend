package com.example.AuthServer.dto;

public class JwtRequest {
    private String token;

    // Constructors, getters, and setters
    public JwtRequest() {}

    public JwtRequest(String token) {
        this.token = token;
    }

    // Getters and setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}