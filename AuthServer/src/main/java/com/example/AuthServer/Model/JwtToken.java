package com.example.AuthServer.Model;

import java.sql.Timestamp;

public class JwtToken {
    private String userId;
    private String token;
    private Timestamp createdAt;
    private Timestamp expiresAt;

    // Constructors, getters, and setters
    public JwtToken() {}

    public JwtToken(String userId, String token, Timestamp createdAt, Timestamp expiresAt) {
        this.userId = userId;
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Timestamp expiresAt) {
        this.expiresAt = expiresAt;
    }
}

