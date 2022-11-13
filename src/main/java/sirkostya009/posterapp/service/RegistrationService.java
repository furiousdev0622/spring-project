package sirkostya009.posterapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sirkostya009.posterapp.model.dao.AppUser;
import sirkostya009.posterapp.model.dao.ConfirmationToken;
import sirkostya009.posterapp.model.common.RegistrationRequest;
import sirkostya009.posterapp.repo.ConfirmationTokenRepo;
import sirkostya009.posterapp.repo.UserRepo;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final ConfirmationTokenRepo tokenRepo;
    private final UserRepo userRepo;
    private final PasswordEncoder encoder;

    @Transactional
    public void confirm(String token) {
        var confirmationToken = tokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("token " + token + " not found"));

        if (confirmationToken.getConfirmedAt() != null)
            throw new RuntimeException("token " + token + " outdated");

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new RuntimeException("token " + token + " expired");

        confirmationToken.getAppUser().enable();

        confirmationToken.setConfirmedAt(LocalDateTime.now());
    }

    public String register(RegistrationRequest request) {
        var user = new AppUser(request.getEmail(), request.getUsername(), request.getPassword());

        if (userRepo.findByUsername(user.getUsername()).isPresent())
            throw new RuntimeException("username already taken");

        if (userRepo.findByEmail(user.getEmail()).isPresent())
            throw new RuntimeException("email already taken");

        user.setPassword(encoder.encode(user.getPassword()));

        userRepo.save(user);

        return tokenRepo.save(new ConfirmationToken(
                UUID.randomUUID().toString(),
                user,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15)
        )).getToken();
    }

}
