package id.ac.ui.cs.advprog.buildingstore.auth.config;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import id.ac.ui.cs.advprog.buildingstore.auth.model.Role;
import id.ac.ui.cs.advprog.buildingstore.auth.model.User;

@SpringBootTest
public class AuthorizationTest {

    @Autowired
    private RoleHierarchy roleHierarchy;
        
    @Test
    void adminAccessCashierMethodsTest() {
        // Create admin authority
        SimpleGrantedAuthority adminAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
        Collection<SimpleGrantedAuthority> adminAuthorities = 
            (Collection<SimpleGrantedAuthority>) roleHierarchy.getReachableGrantedAuthorities(
                Collections.singleton(adminAuthority));
        
        // Testing whether admin can access CASHIER methods
        boolean hasCashierRole = adminAuthorities.stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_CASHIER"));
            
        assertTrue(hasCashierRole, "Admin should be able to access CASHIER methods");
    }
    
    @Test
    void cashierNotAccessAdminMethods() {
        // Create cashier authority
        SimpleGrantedAuthority cashierAuthority = new SimpleGrantedAuthority("ROLE_CASHIER");
        Collection<SimpleGrantedAuthority> cashierAuthorities = 
            (Collection<SimpleGrantedAuthority>) roleHierarchy.getReachableGrantedAuthorities(
                Collections.singleton(cashierAuthority));
        
        // Testing whether cashier can NOT access ADMIN methods
        boolean hasAdminRole = cashierAuthorities.stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            
        assertFalse(hasAdminRole, "Cashier shouldn't be able to access ADMIN methods");
    }
        
    @Test
    void userModelShouldProvideCorrectRoleAuthorities() {
        User cashierUser = User.builder()
                .id(1)
                .username("cashier")
                .password("password")
                .role(Role.CASHIER)
                .build();
                
        User adminUser = User.builder()
                .id(2)
                .username("admin")
                .password("password")
                .role(Role.ADMIN)
                .build();
        
        // Check cashier authorities
        Collection<? extends GrantedAuthority> cashierAuthorities = cashierUser.getAuthorities();
        assertTrue(cashierAuthorities.contains(new SimpleGrantedAuthority("ROLE_CASHIER")));
        assertFalse(cashierAuthorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        
        // Check admin authorities
        Collection<? extends GrantedAuthority> adminAuthorities = adminUser.getAuthorities();
        assertTrue(adminAuthorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }
    
    @Test
    void unauthenticatedUsersHaveNoRoles() {
        // Test if an unauthenticated user has no roles
        Collection<SimpleGrantedAuthority> anonymousAuthorities = 
            Collections.emptyList();
        
        boolean hasAdminRole = anonymousAuthorities.stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean hasCashierRole = anonymousAuthorities.stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_CASHIER"));
            
        assertFalse(hasAdminRole, "Unauthenticated users shouldn't have ADMIN role");
        assertFalse(hasCashierRole, "Unauthenticated users shouldn't have CASHIER role");
    }
    
    @Test
    void userWithInvalidRoleShouldNotHaveAccess() {
        // Test that a user with an non-existent role doesn't get access
        SimpleGrantedAuthority invalidAuthority = new SimpleGrantedAuthority("ROLE_IMPOSTER");
        Collection<SimpleGrantedAuthority> invalidRoleAuthorities = 
            (Collection<SimpleGrantedAuthority>) roleHierarchy.getReachableGrantedAuthorities(
                Collections.singleton(invalidAuthority));
        
        boolean hasAdminRole = invalidRoleAuthorities.stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean hasCashierRole = invalidRoleAuthorities.stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_CASHIER"));
            
        assertFalse(hasAdminRole, "Invalid role shouldn't have ADMIN access");
        assertFalse(hasCashierRole, "Invalid role shouldn't have CASHIER access");
    }
    
    @Test
    void nullUserHasNoAuthorities() {
        User nullUser = null;
        
        try {
            Collection<? extends GrantedAuthority> authorities = nullUser.getAuthorities();
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }
    
    @Test
    void userWithNullRoleShouldThrowException() {
        User userWithNullRole = User.builder()
                .id(3)
                .username("unemployee")
                .password("password")
                .role(null)
                .build();
                
        assertThrows(NullPointerException.class, () -> {
            userWithNullRole.getAuthorities();
        }, "User with null role should throw NullPointerException");
    }
}