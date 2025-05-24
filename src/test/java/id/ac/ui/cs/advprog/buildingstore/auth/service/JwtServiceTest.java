package id.ac.ui.cs.advprog.buildingstore.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
    private JwtService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() throws Exception {
        jwtService = new JwtService();

        java.lang.reflect.Field field = JwtService.class.getDeclaredField("secretKey");
        field.setAccessible(true);
        //this is not a secret, this is a testing-purpose sign in key
        field.set(jwtService, "ad0e022fb6881a5a57d3d885e60f67c500189ba4e2ec5a0092971dd07ec95673");

        userDetails = new User("testUser", "password", List.of());
    }

    @Test
    void testGenerateToken() {
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
    }

    @Test
    void testExtractUsername() {
        String token = jwtService.generateToken(userDetails);
        String extractedUsername = jwtService.extractUsername(token);
        assertEquals("testUser", extractedUsername);
    }

    @Test
    void testIsTokenValid() {
        String token = jwtService.generateToken(userDetails);
        assertTrue(jwtService.IsTokenValid(token, userDetails));
    }

    @Test
    void testTokenNotExpired() {
        String token = jwtService.generateToken(userDetails);
        assertTrue(jwtService.IsTokenValid(token, userDetails));
    }
}