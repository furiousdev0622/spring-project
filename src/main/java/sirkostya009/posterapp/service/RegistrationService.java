package sirkostya009.posterapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sirkostya009.posterapp.email.ConfirmationEmailSender;
import sirkostya009.posterapp.email.EmailSender;
import sirkostya009.posterapp.model.common.RegistrationRequest;
import sirkostya009.posterapp.model.dao.AppUser;
import sirkostya009.posterapp.model.dao.ConfirmationToken;
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
    private final EmailSender emailSender;

    @Transactional
    public void confirm(String token) {
        var confirmationToken = tokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("token " + token + " not found"));

        if (confirmationToken.getConfirmedAt() != null)
            throw new RuntimeException("token " + token + " outdated");

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new RuntimeException("token " + token + " expired");

        confirmationToken.getAppUser().enable();
        confirmationToken.getAppUser().setEmail(confirmationToken.getEmail());

        confirmationToken.setConfirmedAt(LocalDateTime.now());
    }

    public String register(RegistrationRequest request) {
        if (userRepo.findByUsername(request.getUsername()).isPresent())
            throw new RuntimeException("username already taken");

        if (userRepo.findByEmail(request.getEmail()).isPresent())
            throw new RuntimeException("email already taken");

        var user = userRepo.save(new AppUser(
                null,
                request.getUsername(),
                encoder.encode(request.getPassword())
        ));

        var token = tokenRepo.save(new ConfirmationToken(
                UUID.randomUUID().toString(),
                user,
                request.getEmail(),
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15)
        )).getToken();

        emailSender.send(request.getEmail(), "Confirm email", ConfirmationEmailSender.generateMail(token));

        return token;
    }

}
