package com.example.AuthServer.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Override
    public String generateToken(String sessionId, String username, String secretKey) {
        List<String> privileges;

        // Determine privileges based on username
        if ("admin".equals(username)) {
            privileges = List.of("a1", "a2", "a3");
        } else if ("user".equals(username)) {
            privileges = List.of("a1", "a2");
        } else {
            throw new IllegalArgumentException("Invalid username");
        }

        return Jwts.builder()
                .setSubject(username) // Set the subject to username
                .claim("privileges", privileges) // Add privileges claim
                .setId(UUID.randomUUID().toString()) // Set a unique identifier (jti)
                .setIssuedAt(new Date()) // Set issued at (iat)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Set expiration time (exp)
                .signWith(SECRET_KEY) // Sign the token with the secret key
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    @Override
    public List<String> extractPrivileges(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
        return claims.get("privileges", List.class);
    }

    @Override
    public void invalidateToken(String token) {
        // In a real-world scenario, you'd store tokens in a database or cache
        // and mark them as invalidated. Here we just simulate invalidation.
        // For a stateless JWT setup, there's no direct way to invalidate a token.
    }
}
