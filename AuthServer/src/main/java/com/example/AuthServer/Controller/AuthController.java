package com.example.AuthServer.Controller;

import com.example.AuthServer.dto.JwtRequest;
import com.example.AuthServer.dto.JwtResponse;
import com.example.AuthServer.Model.JwtToken;
import com.example.AuthServer.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateToken(@RequestParam("sessionId") String sessionId,
                                           @RequestHeader("Authorization") String authorization) {
        // Decode and extract username and secretKey from Basic Auth
        String[] credentials = extractCredentials(authorization);
        String username = credentials[0];
        String secretKey = credentials[1];

        // Dummy user validation
        if (("user".equals(username) && "userSecret".equals(secretKey)) ||
                ("admin".equals(username) && "adminSecret".equals(secretKey))) {

            // Set privileges based on username
            List<String> privileges;
            if ("admin".equals(username)) {
                privileges = List.of("a1", "a2", "a3");
            } else {
                privileges = List.of("a1", "a2");
            }

            // Generate token
            String token = authService.generateToken(sessionId, username, secretKey);
            return ResponseEntity.ok(Map.of("token", token));
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid Username, SecretKey, or SessionId");
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    private String[] extractCredentials(String authorization) {
        if (authorization != null && authorization.startsWith("Basic ")) {
            String base64Credentials = authorization.substring(6);
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));
            return credentials.split(":", 2);
        }
        throw new RuntimeException("Invalid Authorization header");
    }


    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestBody JwtRequest request) {
        String token = request.getToken();
        boolean isValid = authService.validateToken(token);

        if (isValid) {
            String username = authService.extractUsername(token);
            List<String> privileges = authService.extractPrivileges(token);
            Map<String, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("privileges", privileges);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid Token"));
        }
    }

    @PostMapping("/invalidate")
    public ResponseEntity<?> invalidateToken(@RequestBody JwtRequest request) {
        authService.invalidateToken(request.getToken());
        return ResponseEntity.ok("Token invalidated successfully");
    }
}
