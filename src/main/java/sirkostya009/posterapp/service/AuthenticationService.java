package sirkostya009.posterapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sirkostya009.posterapp.jwt.JwtGenerator;
import sirkostya009.posterapp.model.common.Credentials;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JwtGenerator generator;
    private final PasswordEncoder encoder;

    public String authenticate(Credentials credentials) {
        var user = userService.findByLogin(credentials.getLogin());

        if (!user.isEnabled())
            throw new IllegalStateException("user " + user.getUsername() + " disabled");

        if (!encoder.matches(credentials.getPassword(), user.getPassword()))
            throw new IllegalStateException("password incorrect");

        return generator.generate(user.getUsername());
    }

}
