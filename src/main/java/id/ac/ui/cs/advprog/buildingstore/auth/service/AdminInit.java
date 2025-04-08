package id.ac.ui.cs.advprog.buildingstore.auth.service;

import id.ac.ui.cs.advprog.buildingstore.auth.model.Role;
import id.ac.ui.cs.advprog.buildingstore.auth.model.User;
import id.ac.ui.cs.advprog.buildingstore.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInit implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String adminUsername = "ghaza_admin";

        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            User adminUser = User.builder()
                    .username(adminUsername)
                    .password(passwordEncoder.encode("1738"))
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(adminUser);
            System.out.println("Created default admin user: ghaza_admin");
        } else {
            System.out.println("Admin user already exists: ghaza_admin");
        }
    }
}
