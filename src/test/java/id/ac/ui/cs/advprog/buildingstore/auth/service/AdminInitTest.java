package id.ac.ui.cs.advprog.buildingstore.auth.service;

import id.ac.ui.cs.advprog.buildingstore.auth.model.Role;
import id.ac.ui.cs.advprog.buildingstore.auth.model.User;
import id.ac.ui.cs.advprog.buildingstore.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminInitTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AdminInit adminInit;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        adminInit = new AdminInit(userRepository, passwordEncoder);
    }

    @Test
    void shouldCreateAdminUserIfNotExists() {
        when(userRepository.findByUsername("ghaza_admin")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("1738")).thenReturn("encoded-password");

        adminInit.run();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("ghaza_admin", savedUser.getUsername());
        assertEquals("encoded-password", savedUser.getPassword());
        assertEquals(Role.ADMIN, savedUser.getRole());
    }

    @Test
    void shouldNotCreateAdminUserIfAlreadyExists() {
        User existingUser = User.builder().username("ghaza_admin").build();
        when(userRepository.findByUsername("ghaza_admin")).thenReturn(Optional.of(existingUser));

        adminInit.run();

        verify(userRepository, never()).save(any());
    }
}
