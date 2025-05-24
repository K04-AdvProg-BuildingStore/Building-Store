package id.ac.ui.cs.advprog.buildingstore.auth.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

import jakarta.servlet.Filter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityConfigurationTest {

    @Test
    void testSecurityFilterChain() throws Exception {
        AuthenticationProvider authProvider = mock(AuthenticationProvider.class);
        JwtAuthenticationFilter jwtFilter = mock(JwtAuthenticationFilter.class);
        HttpSecurity http = mock(HttpSecurity.class);

        DefaultSecurityFilterChain realFilterChain =
                new DefaultSecurityFilterChain(AnyRequestMatcher.INSTANCE, new Filter[]{mock(Filter.class)});

        when(http.csrf(any())).thenReturn(http);
        when(http.authorizeHttpRequests(any())).thenReturn(http);
        when(http.sessionManagement(any())).thenReturn(http);
        when(http.authenticationProvider(any())).thenReturn(http);
        when(http.addFilterBefore(any(), any())).thenReturn(http);
        when(http.build()).thenReturn(realFilterChain);

        SecurityConfiguration config = new SecurityConfiguration(authProvider);
        SecurityFilterChain filterChain = config.securityFilterChain(http, jwtFilter);

        assertNotNull(filterChain);
        verify(http).csrf(any());
        verify(http).authorizeHttpRequests(any());
        verify(http).sessionManagement(any());
        verify(http).authenticationProvider(authProvider);
        verify(http).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}