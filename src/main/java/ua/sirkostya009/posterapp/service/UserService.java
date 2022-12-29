package ua.sirkostya009.posterapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ua.sirkostya009.posterapp.email.ConfirmationEmailSender;
import ua.sirkostya009.posterapp.email.EmailSender;
import ua.sirkostya009.posterapp.exception.NotFoundException;
import ua.sirkostya009.posterapp.dto.ChangeSettingsRequest;
import ua.sirkostya009.posterapp.dao.AppUser;
import ua.sirkostya009.posterapp.dao.ConfirmationToken;
import ua.sirkostya009.posterapp.repo.ConfirmationTokenRepo;
import ua.sirkostya009.posterapp.repo.UserRepo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final ConfirmationTokenRepo tokenRepo;
    private final EmailSender emailSender;
    private final PasswordEncoder encoder;

    public final static String IMAGES_PATH = "D:/server/images/";

    @Override
    public UserDetails loadUserByUsername(String username) {
        return findByUsername(username);
    }

    public AppUser findByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("user with username " + username + " could not be found"));
    }

    /**
     * @deprecated reserved for future use
     */
    public AppUser findById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("user with id " + id + " could not be found"));
    }

    @Transactional
    public void edit(ChangeSettingsRequest request, String username) {
        findByUsername(username).setBio(request.getBio());

        if (request.getNewEmail() != null)
            changeEmail(request.getNewEmail(), username);

        if (request.getNewPassword() != null)
            changePassword(request.getNewPassword(), request.getOldPassword(), username);
    }

    public AppUser findByLogin(String login) {
        return userRepo.findByUsernameOrEmail(login, login)
                .orElseThrow(() -> new NotFoundException("user with login " + login + " could not be found"));
    }

    /**
     * @deprecated reserved for future use
     */
    public AppUser findByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("email " + email + " not found"));
    }

    @Transactional
    public void saveImage(MultipartFile file, String username) throws IOException {
        // TODO implement compression and cropping (if not 1:1) procedures
        var originalName = file.getOriginalFilename();
        var fileName = UUID.randomUUID() + originalName.substring(originalName.lastIndexOf('.'));
        Files.write(Path.of(IMAGES_PATH + fileName), file.getBytes());
        findByUsername(username).setProfilePictureFilename(fileName);
    }

    @Transactional
    public boolean changeEmail(String newEmail, String username) {
        var token = tokenRepo.save(new ConfirmationToken(
                UUID.randomUUID().toString(),
                findByUsername(username),
                newEmail,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15)
        )).getToken();

        emailSender.send(newEmail, "Confirm new email", ConfirmationEmailSender.generateBody(token));

        return true;
    }

    @Transactional
    public boolean changePassword(String newPassword, String oldPassword, String username) {
        var user = findByUsername(username);

        if (!encoder.matches(oldPassword, user.getPassword())) return false;

        user.setPassword(encoder.encode(newPassword));

        return true;
    }

    @Transactional
    public void follow(String username, String name) {
        var follower = findByUsername(name);
        var user = findByUsername(username);

        follower.getFollowing().add(user);
        user.getFollowers().add(follower);
    }

    @Transactional
    public void unfollow(String username, String name) {
        var follower = findByUsername(name);
        var user = findByUsername(username);

        follower.getFollowing().remove(user);
        user.getFollowers().remove(follower);
    }
}
