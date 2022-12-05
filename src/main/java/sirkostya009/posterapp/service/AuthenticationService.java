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

    /**
     * Finds a user by login (email or username match),
     * checks if user is enabled,
     * checks if user's password matches the provided one,
     * then generates a bearer token
     * @param credentials a wrapper with long and password fields
     * @return bearer token
     * @throws IllegalStateException if any of the above conditions are false
     */
    public String authenticate(Credentials credentials) {
        var user = userService.findByLogin(credentials.getLogin());

        if (!user.isEnabled())
            throw new IllegalStateException("user " + user.getUsername() + " disabled");

        if (!encoder.matches(credentials.getPassword(), user.getPassword()))
            throw new IllegalStateException("password incorrect");

        return generator.generate(user.getUsername());
    }

}
