package sirkostya009.posterapp.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sirkostya009.posterapp.model.publicized.Credentials;
import sirkostya009.posterapp.model.publicized.UserInfo;
import sirkostya009.posterapp.service.AuthenticationService;
import sirkostya009.posterapp.service.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserApi {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public String authenticate(@RequestBody Credentials credentials) {
        return authenticationService.authenticate(credentials);
    }

    @GetMapping("/{username}")
    public UserInfo user(@PathVariable String username) {
        return UserInfo.fromAppUser(userService.findByUsername(username));
    }

    @GetMapping("/self")
    public UserInfo self(JwtAuthenticationToken token) {
        return UserInfo.fromAppUser(userService.findByUsername(token.getName()), true);
    }

    @PostMapping("/edit")
    public void edit(@RequestBody UserInfo info,
                     JwtAuthenticationToken token) {
        userService.edit(info, token.getName());
    }

    @PostMapping("/change-email")
    public void changeEmail(@RequestBody String newEmail,
                            JwtAuthenticationToken token) {
        userService.changeEmail(newEmail, token.getName());
    }

    @PostMapping("/change-password")
    public void changePassword(@RequestBody String newPassword,
                               JwtAuthenticationToken token) {
        userService.changePassword(newPassword, token.getName());
    }

    @GetMapping(value = "/photo/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] photo(@PathVariable String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(userService.ImagesPath + fileName));
    }

    @PostMapping("/photo-upload")
    public void uploadPhoto(@RequestParam("image") MultipartFile file,
                            JwtAuthenticationToken token) throws IOException {
        userService.saveImage(file, token.getName());
    }

}
