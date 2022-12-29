package ua.sirkostya009.posterapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.sirkostya009.posterapp.dao.ConfirmationToken;

import java.util.Optional;

public interface ConfirmationTokenRepo extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByToken(String token);
}
