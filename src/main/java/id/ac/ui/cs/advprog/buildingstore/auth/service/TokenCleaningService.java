package id.ac.ui.cs.advprog.buildingstore.auth.service;

import id.ac.ui.cs.advprog.buildingstore.auth.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenCleaningService {

    private final TokenRepository tokenRepository;

    // runs every day at midnight
    @Scheduled(cron = "0 0 0 * * *")
    public void cleanExpiredOrRevokedTokens() {
        int deletedCount = tokenRepository.deleteByExpiredTrueOrRevokedTrue();
        if (deletedCount > 0) {
            System.out.println("Cleaned up " + deletedCount + " expired/revoked tokens");
        }
    }
}
