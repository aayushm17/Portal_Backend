package com.example.AuthServer;

import com.example.AuthServer.Controller.AuthController;
import com.example.AuthServer.dto.JwtRequest;
import com.example.AuthServer.dto.JwtResponse;
import com.example.AuthServer.Service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Base64;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGenerateToken() {
        // Prepare the mock authorization header and sessionId
        String sessionId = "testSessionId";
        String authorizationHeader = "Basic " + Base64.getEncoder().encodeToString("user:userSecret".getBytes());

        // Expected token to be returned by the service
        String expectedToken = "generatedToken";

        // Mock the AuthService to return the expected token
        when(authService.generateToken(sessionId, "user", "userSecret")).thenReturn(expectedToken);

        // Call the controller method
        ResponseEntity<?> response = authController.generateToken(sessionId, authorizationHeader);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedToken, ((Map<String, Object>) response.getBody()).get("token"));

        // Verify that the service was called with the correct parameters
        verify(authService, times(1)).generateToken(sessionId, "user", "userSecret");
    }

    @Test
    public void testValidateToken() {
        JwtRequest request = new JwtRequest("testToken");

        String username = "user";
        List<String> privileges = List.of("a1", "a2");

        when(authService.validateToken("testToken")).thenReturn(true);
        when(authService.extractUsername("testToken")).thenReturn(username);
        when(authService.extractPrivileges("testToken")).thenReturn(privileges);

        ResponseEntity<?> response = authController.validateToken(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals(username, responseBody.get("username"));
        assertEquals(privileges, responseBody.get("privileges"));

        verify(authService, times(1)).validateToken("testToken");
        verify(authService, times(1)).extractUsername("testToken");
        verify(authService, times(1)).extractPrivileges("testToken");
    }


    @Test
    public void testInvalidateToken() {
        JwtRequest request = new JwtRequest("testToken");

        doNothing().when(authService).invalidateToken("testToken");

        ResponseEntity<?> response = authController.invalidateToken(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Token invalidated successfully", response.getBody());

        verify(authService, times(1)).invalidateToken("testToken");
    }
}
