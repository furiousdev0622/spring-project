package sirkostya009.posterapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sirkostya009.posterapp.model.common.AppUserModel;
import sirkostya009.posterapp.model.dao.AppUser;
import sirkostya009.posterapp.repo.UserRepo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepo repo;

    public final static String IMAGES_PATH = "D:/server/images/";

    @Override
    public UserDetails loadUserByUsername(String username) {
        return findByUsername(username);
    }

    public AppUser findByUsername(String username) {
        return repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username " + username + " not found"));
    }

    public AppUser findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("user with id " + id + " not found"));
    }

    @Transactional
    public void edit(AppUserModel info, String username) {
        findByUsername(username).setBio(info.getBio());
    }

    public AppUser findByLogin(String login) {
        return repo.findByUsernameOrEmail(login, login)
                .orElseThrow(() -> new RuntimeException("login " + login + " not found"));
    }

    public AppUser findByEmail(String email) {
        return repo.findByEmail(email)
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
    public void changeEmail(String newEmail, String name) {
    }

    @Transactional
    public void changePassword(String newPassword, String name) {
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
