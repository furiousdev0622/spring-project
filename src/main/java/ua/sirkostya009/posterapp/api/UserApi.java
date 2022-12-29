package ua.sirkostya009.posterapp.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.sirkostya009.posterapp.dto.AppUserModel;
import ua.sirkostya009.posterapp.dto.ChangeSettingsRequest;
import ua.sirkostya009.posterapp.dto.Credentials;
import ua.sirkostya009.posterapp.exception.AuthenticationException;
import ua.sirkostya009.posterapp.exception.NotFoundException;
import ua.sirkostya009.posterapp.service.AuthenticationService;
import ua.sirkostya009.posterapp.service.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserApi {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    /**
     * Acquires user credentials and returns a bearer token
     * @param credentials a simple wrapper with login and password fields
     * @return bearer token that expires in 14 days
     * @throws AuthenticationException if user is disabled or credentials are incorrect
     */
    @PostMapping("/login")
    public String authenticate(@RequestBody Credentials credentials) {
        return authenticationService.authenticate(credentials);
    }

    /**
     * Returns information of a specified user
     * @param username requested user's information username
     * @return user
     * @throws NotFoundException if no user was found
     */
    @GetMapping("/{username}")
    public AppUserModel user(@PathVariable String username) {
        return AppUserModel.of(userService.findByUsername(username), false);
    }

    /**
     * Returns information about the requesting user
     * @param token an object that holds current user's username
     * @return user
     */
    @GetMapping("/self")
    public AppUserModel self(JwtAuthenticationToken token) {
        return AppUserModel.of(userService.findByUsername(token.getName()), true);
    }

    /**
     * Changes bio and email of the user
     * @param request an AppUserModel instance that holds all public user info
     * @param token an object that holds current user's username
     */
    @PostMapping("/edit")
    public void edit(@RequestBody ChangeSettingsRequest request,
                     JwtAuthenticationToken token) {
        userService.edit(request, token.getName());
    }

    /**
     * Makes the current user follow a specified user
     * @param username the user to follow
     * @param token an object that holds current user's username
     */
    @PostMapping("/follow/{username}")
    public void follow(@PathVariable String username,
                       JwtAuthenticationToken token) {
        userService.follow(username, token.getName());
    }

    /**
     * Makes the current user no longer follow a specified user
     * @param username the user to unfollow
     * @param token an object that holds current user's username
     */
    @PostMapping("/unfollow/{username}")
    public void unfollow(@PathVariable String username,
                         JwtAuthenticationToken token) {
        userService.unfollow(username, token.getName());
    }

    /**
     * Requests to change email of the current user
     * @param request a request wrapper with newEmail field
     * @param token an object that holds current user's username
     * @return always true
     */
    @PostMapping("/change-email")
    public boolean changeEmail(@RequestBody ChangeSettingsRequest request,
                            JwtAuthenticationToken token) {
        return userService.changeEmail(request.getNewEmail(), token.getName());
    }

    /**
     * Changes current user's password to newPassword if oldPassword matches the current one
     * @param request a request wrapper with newPassword and oldPassword fields
     * @param token an object that holds current user's username
     * @return true if password has been changed successfully, otherwise false
     */
    @PostMapping("/change-password")
    public boolean changePassword(@RequestBody ChangeSettingsRequest request,
                               JwtAuthenticationToken token) {
        return userService.changePassword(request.getNewPassword(), request.getOldPassword(), token.getName());
    }

    /**
     * Returns an image with a specified filename
     * @param fileName filename of the image, usually got from supplied user info
     * @return an array of bytes that make up the file
     * @throws IOException if file could not be found
     */
    @GetMapping(value = "/photo/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] photo(@PathVariable String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(UserService.IMAGES_PATH + fileName));
    }

    /**
     * Saves and sets an uploaded image as user's profile picture
     * @param file a multipart file that is an image (currently doesn't check if it's an image at all)
     * @param token an object that holds current user's username
     * @throws IOException if file couldn't be written
     */
    @PostMapping("/photo-upload")
    public void uploadPhoto(@RequestParam("image") MultipartFile file,
                            JwtAuthenticationToken token) throws IOException {
        userService.saveImage(file, token.getName());
    }

}
