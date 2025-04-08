package id.ac.ui.cs.advprog.buildingstore.auth.service;

import id.ac.ui.cs.advprog.buildingstore.auth.repository.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class TokenCleaningServiceTest {

    private TokenRepository tokenRepository;
    private TokenCleaningService tokenCleaningService;

    @BeforeEach
    void setUp() {
        tokenRepository = mock(TokenRepository.class);
        tokenCleaningService = new TokenCleaningService(tokenRepository);
    }

    @Test
    void DeleteExistingTokens() {
        when(tokenRepository.deleteByExpiredTrueOrRevokedTrue()).thenReturn(5);
        tokenCleaningService.cleanExpiredOrRevokedTokens();
        verify(tokenRepository, times(1)).deleteByExpiredTrueOrRevokedTrue();
    }

    @Test
    void StillCleanWhenNoTokensExist() {
        when(tokenRepository.deleteByExpiredTrueOrRevokedTrue()).thenReturn(0);
        tokenCleaningService.cleanExpiredOrRevokedTokens();
        verify(tokenRepository, times(1)).deleteByExpiredTrueOrRevokedTrue();
    }
}
