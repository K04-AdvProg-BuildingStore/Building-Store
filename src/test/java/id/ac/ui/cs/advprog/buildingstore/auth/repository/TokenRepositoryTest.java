package id.ac.ui.cs.advprog.buildingstore.auth.repository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import id.ac.ui.cs.advprog.buildingstore.auth.model.Role;
import id.ac.ui.cs.advprog.buildingstore.auth.model.Token;
import id.ac.ui.cs.advprog.buildingstore.auth.model.User;

@DataJpaTest
class TokenRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Token validToken;
    private Token expiredToken;
    private Token revokedToken;
    private Token expiredAndRevokedToken;

    @BeforeEach
    void setUp() {
        // Create test user
        testUser = User.builder()
                .username("MinecraftSteve")
                .password("password123")
                .role(Role.CASHIER)
                .build();
        userRepository.save(testUser);

        validToken = Token.builder()
                .token("valid-token")
                .expired(false)
                .revoked(false)
                .user(testUser)
                .build();

        expiredToken = Token.builder()
                .token("expired-token")
                .expired(true)
                .revoked(false)
                .user(testUser)
                .build();

        revokedToken = Token.builder()
                .token("revoked-token")
                .expired(false)
                .revoked(true)
                .user(testUser)
                .build();

        expiredAndRevokedToken = Token.builder()
                .token("expired-revoked-token")
                .expired(true)
                .revoked(true)
                .user(testUser)
                .build();

        // Save tokens
        entityManager.persist(validToken);
        entityManager.persist(expiredToken);
        entityManager.persist(revokedToken);
        entityManager.persist(expiredAndRevokedToken);
        entityManager.flush();
    }

    @Test
    void findAllByUser_IdAndExpiredFalseAndRevokedFalse_ShouldReturnOnlyValidTokens() {
        // When
        List<Token> result = tokenRepository.findAllByUser_IdAndExpiredFalseAndRevokedFalse(testUser.getId());

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getToken()).isEqualTo("valid-token");
        assertThat(result.get(0).isExpired()).isFalse();
        assertThat(result.get(0).isRevoked()).isFalse();
    }

    @Test
    void findAllByUser_IdAndExpiredFalseAndRevokedFalse_WithNoValidTokens_ShouldReturnEmptyList() {
        User anotherUser = User.builder()
                .username("MinecraftAlex")
                .password("password456")
                .role(Role.CASHIER)
                .build();
        userRepository.save(anotherUser);

        // When
        List<Token> result = tokenRepository.findAllByUser_IdAndExpiredFalseAndRevokedFalse(anotherUser.getId());

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void findByToken_WithExistingToken_ShouldReturnToken() {
        // When
        Optional<Token> result = tokenRepository.findByToken("valid-token");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getToken()).isEqualTo("valid-token");
        assertThat(result.get().getId()).isEqualTo(validToken.getId());
    }

    @Test
    void findByToken_WithNonExistingToken_ShouldReturnEmptyOptional() {
        // When
        Optional<Token> result = tokenRepository.findByToken("non-existing-token");

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void tokenPersistence_ShouldStoreAndRetrieveCorrectly() {
        // Given
        String newTokenValue = "new-test-token";
        Token newToken = Token.builder()
                .token(newTokenValue)
                .expired(false)
                .revoked(false)
                .user(testUser)
                .build();

        // When
        Token savedToken = tokenRepository.save(newToken);
        Optional<Token> retrievedToken = tokenRepository.findById(savedToken.getId());

        // Then
        assertThat(retrievedToken).isPresent();
        assertThat(retrievedToken.get().getToken()).isEqualTo(newTokenValue);
        assertThat(retrievedToken.get().getUser().getId()).isEqualTo(testUser.getId());
    }

    @Test
    void saveAll_ShouldPersistMultipleTokens() {
        // Given
        tokenRepository.deleteAll();
        
        Token token1 = Token.builder()
                .token("token1")
                .expired(false)
                .revoked(false)
                .user(testUser)
                .build();
                
        Token token2 = Token.builder()
                .token("token2")
                .expired(false)
                .revoked(false)
                .user(testUser)
                .build();
        
        List<Token> tokens = List.of(token1, token2);

        // When
        tokenRepository.saveAll(tokens);
        List<Token> savedTokens = tokenRepository.findAll();

        // Then
        assertThat(savedTokens).hasSize(2);
        assertThat(savedTokens).extracting(Token::getToken).containsExactlyInAnyOrder("token1", "token2");
    }

    @Test
    void findAllByUser_IdAndExpiredFalseAndRevokedFalse_ShouldNotReturnExpiredTokens() {
        // When
        List<Token> result = tokenRepository.findAllByUser_IdAndExpiredFalseAndRevokedFalse(testUser.getId());

        // Then
        assertThat(result).hasSize(1);
        assertThat(result).extracting(Token::getToken).doesNotContain("expired-token", "expired-revoked-token");
    }

    @Test
    void findAllByUser_IdAndExpiredFalseAndRevokedFalse_ShouldNotReturnRevokedTokens() {
        // When
        List<Token> result = tokenRepository.findAllByUser_IdAndExpiredFalseAndRevokedFalse(testUser.getId());

        // Then
        assertThat(result).hasSize(1);
        assertThat(result).extracting(Token::getToken).doesNotContain("revoked-token", "expired-revoked-token");
    }
}