package id.ac.ui.cs.advprog.buildingstore.auth.service;

import id.ac.ui.cs.advprog.buildingstore.auth.controller.AuthenticationRequest;
import id.ac.ui.cs.advprog.buildingstore.auth.controller.AuthenticationResponse;
import id.ac.ui.cs.advprog.buildingstore.auth.controller.RegisterRequest;
import id.ac.ui.cs.advprog.buildingstore.auth.model.Role;
import id.ac.ui.cs.advprog.buildingstore.auth.model.User;
import id.ac.ui.cs.advprog.buildingstore.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        User savedUser = User.builder()
                .username("testuser")
                .password("encoded-password")
                .role(Role.USER)
                .build();

        when(passwordEncoder.encode("password")).thenReturn("encoded-password");
        when(repository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.generateToken(savedUser)).thenReturn("test-token");

        AuthenticationResponse response = service.register(request);

        assertEquals("test-token", response.getToken());
        verify(passwordEncoder).encode("password");
        verify(repository).save(any(User.class));
        verify(jwtService).generateToken(savedUser);
    }

    @Test
    void testAuthenticate() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        User user = User.builder()
                .username("testuser")
                .password("encoded-password")
                .role(Role.USER)
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(repository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("test-token");

        AuthenticationResponse response = service.authenticate(request);

        assertEquals("test-token", response.getToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(repository).findByUsername("testuser");
        verify(jwtService).generateToken(user);
    }
}