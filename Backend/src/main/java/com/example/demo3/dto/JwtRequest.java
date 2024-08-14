package com.example.demo3.dto;

public class JwtRequest {
    private String sessionId;

    // Constructors, getters, and setters
    public JwtRequest() {}

    public JwtRequest(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
