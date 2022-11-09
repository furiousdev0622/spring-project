package sirkostya009.posterapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import sirkostya009.posterapp.model.privatized.ConfirmationToken;

import java.util.Optional;

public interface ConfirmationTokenRepo extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findFirstByToken(String token);
}
