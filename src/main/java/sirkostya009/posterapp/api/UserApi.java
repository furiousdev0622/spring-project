package sirkostya009.posterapp.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sirkostya009.posterapp.model.AppUser;
import sirkostya009.posterapp.model.Credentials;
import sirkostya009.posterapp.model.UserInfo;
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

    @PostMapping("/authenticate")
    public String authenticate(@RequestBody Credentials credentials) {
        return authenticationService.authenticate(credentials);
    }

    @GetMapping("/{username}")
    public UserInfo getUser(@PathVariable String username) {
        return UserInfo.fromAppUser((AppUser) userService.loadUserByUsername(username));
    }

    @GetMapping("/self")
    public UserInfo getUserInfo(@AuthenticationPrincipal AppUser user) {
        return UserInfo.fromAppUser(user, true);
    }

    @GetMapping(value = "/photo/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getPhoto(@PathVariable String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(userService.ImagesPath + fileName));
    }

    @PostMapping("/photo/upload")
    public void uploadImage(@RequestParam("image") MultipartFile file, @AuthenticationPrincipal AppUser user) throws IOException {
        userService.saveImage(file, user.getUsername());
    }
}
