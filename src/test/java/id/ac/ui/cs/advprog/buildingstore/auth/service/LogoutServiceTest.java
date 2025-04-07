package id.ac.ui.cs.advprog.buildingstore.auth.service;

import id.ac.ui.cs.advprog.buildingstore.auth.model.Token;
import id.ac.ui.cs.advprog.buildingstore.auth.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.mockito.Mockito.*;

class LogoutServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private LogoutService logoutService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    void whenValidBearerToken_thenTokenIsRevokedAndExpired() throws Exception {
        String jwtToken = "valid.jwt.token";
        Token token = new Token();

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);
        when(tokenRepository.findByToken(jwtToken)).thenReturn(Optional.of(token));

        logoutService.logout(request, response);

        verify(tokenRepository).save(token);
        assert token.isExpired();
        assert token.isRevoked();
    }

    @Test
    void whenAuthorizationHeaderIsMissing_thenLogoutDoesNothing() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        logoutService.logout(request, response);

        verify(tokenRepository, never()).save(any());
    }

    @Test
    void whenAuthorizationHeaderIsMalformed_thenLogoutDoesNothing() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("InvalidHeaderContent");

        logoutService.logout(request, response);

        verify(tokenRepository, never()).save(any());
    }

    @Test
    void whenTokenIsNotFoundInRepository_thenLogoutDoesNothing() throws Exception {
        String jwtToken = "non.existent.token";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);
        when(tokenRepository.findByToken(jwtToken)).thenReturn(Optional.empty());

        logoutService.logout(request, response);

        verify(tokenRepository, never()).save(any());
    }
}
