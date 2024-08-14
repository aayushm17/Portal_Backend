package com.example.demo3;

import com.example.demo3.dto.JwtRequest;
import com.example.demo3.services.JwtUtil;
import com.example.demo3.controller.CertificateController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import java.util.*;

public class CertificateControllerTest {

    @InjectMocks
    private CertificateController certificateController;

    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testValidateToken_Success() {
        String token = "Bearer valid-token";
        List<String> roles = List.of("ROLE_USER");
        when(jwtUtil.validateToken(anyString())).thenReturn(true);
        when(jwtUtil.extractRoles(anyString())).thenReturn(roles);

        ResponseEntity<?> response = certificateController.validateToken(token);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Token is valid", ((java.util.Map) response.getBody()).get("message"));
        assertEquals(roles, ((java.util.Map) response.getBody()).get("roles"));
    }

    @Test
    public void testInvalidateToken_Success() {
        String token = "Bearer valid-token";
        List<String> roles = List.of("ROLE_USER");
        when(jwtUtil.invalidateToken(anyString())).thenReturn(true);
        when(jwtUtil.extractRoles(anyString())).thenReturn(roles);

        ResponseEntity<?> response = certificateController.invalidateToken(token);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Token invalidated", ((java.util.Map) response.getBody()).get("message"));
        assertEquals(roles, ((java.util.Map) response.getBody()).get("roles"));
    }

    @Test
    public void testValidateToken_Failure() {
        String token = "Bearer invalid-token";
        when(jwtUtil.validateToken(anyString())).thenReturn(false);

        ResponseEntity<?> response = certificateController.validateToken(token);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid Token", ((java.util.Map) response.getBody()).get("error"));
    }

    @Test
    public void testInvalidateToken_Failure() {
        String token = "Bearer invalid-token";
        when(jwtUtil.invalidateToken(anyString())).thenReturn(false);

        ResponseEntity<?> response = certificateController.invalidateToken(token);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid Token", ((java.util.Map) response.getBody()).get("error"));
    }
}
