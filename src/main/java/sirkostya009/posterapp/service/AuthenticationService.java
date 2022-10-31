package sirkostya009.posterapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import sirkostya009.posterapp.model.Credentials;
import sirkostya009.posterapp.util.JwtUtils;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtUtils jwtUtils;
    private final AuthenticationManager manager;
    private final UserService userService;

    public String authenticate(Credentials credentials) {
        manager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getLogin(), credentials.getPassword())
        );

        var user = userService.findByLogin(credentials.getLogin());

        return jwtUtils.generateToken(user);
    }

}
