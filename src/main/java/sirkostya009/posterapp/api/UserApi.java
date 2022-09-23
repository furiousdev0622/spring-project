package sirkostya009.posterapp.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import sirkostya009.posterapp.model.AppUser;
import sirkostya009.posterapp.model.Credentials;
import sirkostya009.posterapp.model.UserInfo;
import sirkostya009.posterapp.service.UserService;
import sirkostya009.posterapp.util.JwtUtils;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserApi {

    private final UserService service;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager manager;

    @PostMapping("/authenticate")
    public String authenticate(@RequestBody Credentials credentials) {
        try {
            manager.authenticate(
                    new UsernamePasswordAuthenticationToken(credentials.getLogin(), credentials.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Login or password incorrect", e);
        }

        var user = service.findByLogin(credentials.getLogin());

        return jwtUtils.generateToken(user);
    }

    @GetMapping("/{username}")
    public UserInfo getUser(@PathVariable String username) {
        return UserInfo.fromAppUser((AppUser) service.loadUserByUsername(username));
    }

    @GetMapping("/self")
    public UserInfo getUserInfo(UsernamePasswordAuthenticationToken token) {
        return UserInfo.fromAppUser((AppUser) token.getPrincipal());
    }
}
