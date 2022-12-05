package sirkostya009.posterapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sirkostya009.posterapp.email.ConfirmationEmailSender;
import sirkostya009.posterapp.email.EmailSender;
import sirkostya009.posterapp.model.common.ChangeSettingsRequest;
import sirkostya009.posterapp.model.dao.AppUser;
import sirkostya009.posterapp.model.dao.ConfirmationToken;
import sirkostya009.posterapp.repo.ConfirmationTokenRepo;
import sirkostya009.posterapp.repo.UserRepo;

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
                .orElseThrow(() -> new UsernameNotFoundException("username " + username + " not found"));
    }

    /**
     * @deprecated reserved for future use
     */
    public AppUser findById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("user with id " + id + " not found"));
    }

    @Transactional
    public void edit(ChangeSettingsRequest userModel, String username) {
        findByUsername(username).setBio(userModel.getBio());

        if (userModel.getNewEmail() != null)
            changeEmail(userModel.getNewEmail(), username);
    }

    public AppUser findByLogin(String login) {
        return userRepo.findByUsernameOrEmail(login, login)
                .orElseThrow(() -> new RuntimeException("login " + login + " not found"));
    }

    /**
     * @deprecated reserved for future use
     */
    public AppUser findByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("email " + email + " not found"));
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

        emailSender.send(newEmail, "Confirm new email", ConfirmationEmailSender.generateMail(token));

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
