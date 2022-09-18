package sirkostya009.posterapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sirkostya009.posterapp.model.AppUser;
import sirkostya009.posterapp.model.ConfirmationToken;
import sirkostya009.posterapp.registration.RegistrationRequest;
import sirkostya009.posterapp.repo.ConfirmationTokenRepo;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final ConfirmationTokenRepo repo;
    private final UserService userService;

    @Transactional
    public void confirm(String token) {
        var confirmationToken = repo.findFirstByToken(token)
                .orElseThrow(() -> new RuntimeException("token " + token + " not found"));

        if (confirmationToken.getConfirmedAt() != null)
            throw new RuntimeException("token " + token + " outdated");

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new RuntimeException("token " + token + " expired");

        userService.findById(confirmationToken.getUserId()).enable();

        confirmationToken.setConfirmedAt(LocalDateTime.now());
    }

    public String register(RegistrationRequest request) {
        var user = new AppUser(request.getEmail(), request.getUsername(), request.getPassword());

        userService.registerUser(user);

        var token = new ConfirmationToken(
                UUID.randomUUID().toString(),
                user.getId(),
                LocalDateTime.now().plusMinutes(15),
                LocalDateTime.now()
        );

        repo.save(token);

        return token.getToken();
    }
}
