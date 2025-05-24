package id.ac.ui.cs.advprog.buildingstore.auth.repository;

import id.ac.ui.cs.advprog.buildingstore.auth.model.Token;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    List<Token> findAllByUser_IdAndExpiredFalseAndRevokedFalse(Integer userId);
    Optional<Token> findByToken(String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM Token t WHERE t.expired = true OR t.revoked = true")
    int deleteByExpiredTrueOrRevokedTrue();
}
