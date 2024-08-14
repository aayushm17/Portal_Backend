package com.example.AuthServer.Service;

import java.util.List;

public interface AuthService {
    String generateToken(String sessionId, String username, String secretKey);
    String extractUsername(String token);
    List<String> extractPrivileges(String token);
    boolean validateToken(String token);
    void invalidateToken(String token);
}
