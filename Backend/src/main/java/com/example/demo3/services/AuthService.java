package com.example.demo3.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    private final RestTemplate restTemplate;

    @Value("${auth.server.url}")
    private String authServerUrl;

    public AuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String generateToken(String sessionId, String username, String secretKey) {
        String generateUrl = authServerUrl + "/generate?sessionId=" + sessionId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(username, secretKey);  // Set Basic Auth header

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(generateUrl, HttpMethod.POST, requestEntity, (Class<Map<String, Object>>)(Class<?>)Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return (String) response.getBody().get("token");
            } else {
                throw new RuntimeException("Failed to generate token");
            }
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Failed to generate token: " + e.getMessage(), e);
        }
    }


    public ResponseEntity<Map<String, Object>> validate(String token) {
        String validateUrl = authServerUrl + "/validate";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create a body containing the token
        Map<String, String> body = new HashMap<>();
        body.put("token", token);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);

        try {
            return restTemplate.exchange(validateUrl, HttpMethod.POST, requestEntity, (Class<Map<String, Object>>)(Class<?>)Map.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }
    }


    public ResponseEntity<String> invalidate(String token) {
        String invalidateUrl = authServerUrl + "/invalidate";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            return restTemplate.exchange(invalidateUrl, HttpMethod.POST, requestEntity, String.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }
}
