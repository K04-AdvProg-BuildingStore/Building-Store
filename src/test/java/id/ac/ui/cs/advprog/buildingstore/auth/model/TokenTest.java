package id.ac.ui.cs.advprog.buildingstore.auth.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenTest {

    @Test
    void testTokenBuilderCreatesValidToken() {
        User user = User.builder()
                .id(1)
                .username("CashCashMoney")
                .password("123")
                .role(Role.CASHIER)
                .build();

        Token token = Token.builder()
                .id(10)
                .token("valid.jwt.token")
                .revoked(false)
                .expired(false)
                .user(user)
                .build();

        assertEquals(10, token.getId());
        assertEquals("valid.jwt.token", token.getToken());
        assertFalse(token.isRevoked());
        assertFalse(token.isExpired());
        assertEquals(user, token.getUser());
    }

    @Test
    void testSettersAndGettersWorkAsExpected() {
        User user = new User();
        user.setId(2);
        user.setUsername("BigBoss");
        user.setRole(Role.ADMIN);

        Token token = new Token();
        token.setId(20);
        token.setToken("set.token.value");
        token.setRevoked(true);
        token.setExpired(true);
        token.setUser(user);

        assertEquals(20, token.getId());
        assertEquals("set.token.value", token.getToken());
        assertTrue(token.isRevoked());
        assertTrue(token.isExpired());
        assertEquals(user, token.getUser());
    }

    @Test
    void testNullTokenStringIsHandled() {
        Token token = new Token();
        token.setToken(null);

        assertNull(token.getToken());
    }

    @Test
    void testNullUserReferenceIsHandled() {
        Token token = Token.builder()
                .id(99)
                .token("some.token")
                .revoked(false)
                .expired(false)
                .user(null)
                .build();

        assertNull(token.getUser());
    }
}
