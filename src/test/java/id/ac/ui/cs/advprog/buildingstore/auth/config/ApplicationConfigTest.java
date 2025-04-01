package id.ac.ui.cs.advprog.buildingstore.auth.config;

import id.ac.ui.cs.advprog.buildingstore.auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ApplicationConfigTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ApplicationConfig applicationConfig;

    @Test
    void testUserDetailsService() {
        UserDetailsService userDetailsService = applicationConfig.userDetailsService();
        assertThat(userDetailsService).isNotNull();
    }

    @Test
    void testAuthenticationProvider() {
        AuthenticationProvider authenticationProvider = applicationConfig.authenticationProvider();
        assertThat(authenticationProvider).isNotNull();
    }

    @Test
    void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = applicationConfig.passwordEncoder();
        assertThat(passwordEncoder).isNotNull();
    }

    @Test
    void testAuthenticationManager() {
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        assertThat(authenticationManager).isNotNull();
    }
}
