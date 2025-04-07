package id.ac.ui.cs.advprog.buildingstore.auth.repository;

import id.ac.ui.cs.advprog.buildingstore.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);
}
