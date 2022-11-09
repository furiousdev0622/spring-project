package sirkostya009.posterapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sirkostya009.posterapp.jwt.JwtGenerator;
import sirkostya009.posterapp.model.Credentials;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JwtGenerator generator;

    public String authenticate(Credentials credentials) {
        var user = userService.findByLogin(credentials.getLogin());

        if (!user.isEnabled())
            throw new IllegalStateException("user " + user.getUsername() + " disabled");

        return generator.generate(user.getUsername());
    }

}
