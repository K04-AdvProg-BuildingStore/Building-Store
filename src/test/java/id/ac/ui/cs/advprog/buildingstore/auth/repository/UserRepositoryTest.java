package id.ac.ui.cs.advprog.buildingstore.auth.repository;

import id.ac.ui.cs.advprog.buildingstore.auth.model.User;
import id.ac.ui.cs.advprog.buildingstore.BuildingStoreApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = BuildingStoreApplication.class)
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testUser");
        user.setPassword("password"); // Ideally, use an encoded password
        userRepository.save(user);
    }

    @Test
    void testFindByUsername() {
        User foundUser = userRepository.findByUsername("testUser").orElseThrow();
        assertThat(foundUser.getUsername()).isEqualTo("testUser");
    }

    @Test
    void testFindByUsername_NotFound() {
        assertThat(userRepository.findByUsername("nonExistingUser")).isEmpty();
    }
}
