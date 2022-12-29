package ua.sirkostya009.posterapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.sirkostya009.posterapp.dao.AppUser;

import java.util.Optional;

public interface UserRepo extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsernameOrEmail(String username, String email);

    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findByUsername(String username);
}
