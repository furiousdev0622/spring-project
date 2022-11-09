package sirkostya009.posterapp.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import sirkostya009.posterapp.service.UserService;

import javax.crypto.SecretKey;

import static org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames.SUB;
import static org.springframework.security.oauth2.jose.jws.MacAlgorithm.HS384;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {

    public final static String ISSUER = "localhost"; // this is better put somewhere else

    private final SecretKey secretKey;
    private final UserService userService;

    @Bean
    public JwtDecoder jwtDecoder() {
        var decoder = NimbusJwtDecoder
                .withSecretKey(secretKey)
                .macAlgorithm(HS384)
                .build();
        decoder.setJwtValidator(delegatingValidator());
        return decoder;
    }

    private OAuth2TokenValidator<Jwt> delegatingValidator() {
        return new DelegatingOAuth2TokenValidator<>(
                subjectValidator(),
                new JwtTimestampValidator(),
                new JwtIssuerValidator(ISSUER)
        );
    }

    private OAuth2TokenValidator<Jwt> subjectValidator() {
        return new JwtClaimValidator<String>(SUB, sub -> userService.findByUsername(sub).isEnabled());
    }

}
