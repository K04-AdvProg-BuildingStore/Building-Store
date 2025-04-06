package id.ac.ui.cs.advprog.buildingstore.auth.repository;

import id.ac.ui.cs.advprog.buildingstore.auth.model.Token;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    List<Token> findAllByUser_IdAndExpiredFalseAndRevokedFalse(Integer userId);
    Optional<Token> findByToken(String token);
}
