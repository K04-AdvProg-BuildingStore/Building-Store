package id.ac.ui.cs.advprog.buildingstore.auth.controller;

import id.ac.ui.cs.advprog.buildingstore.auth.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationControllerTest {

    @Mock
    private AuthenticationService service;

    @InjectMocks
    private AuthenticationController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        AuthenticationResponse expectedResponse = AuthenticationResponse.builder()
                .token("test-token")
                .build();

        when(service.register(request)).thenReturn(expectedResponse);

        ResponseEntity<AuthenticationResponse> response = controller.register(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(service, times(1)).register(request);
    }

    @Test
    void testAuthenticate() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        AuthenticationResponse expectedResponse = AuthenticationResponse.builder()
                .token("test-token")
                .build();

        when(service.authenticate(request)).thenReturn(expectedResponse);

        ResponseEntity<AuthenticationResponse> response = controller.authenticate(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(service, times(1)).authenticate(request);
    }
}