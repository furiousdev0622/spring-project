package sirkostya009.posterapp.jwt;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sirkostya009.posterapp.security.JwtConfig;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Generates a JWT bearer token
 */
@Component
@RequiredArgsConstructor
public class JwtGenerator {

    private final static long EXPIRATION_TIME_IN_MILLISECONDS = (long) 1.21e+9; // 14 days

    private final SecretKey secretKey;

    public String generate(String subject) {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setSubject(subject)
                .setIssuer(JwtConfig.ISSUER)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_IN_MILLISECONDS))
                .signWith(secretKey)
                .compact();
    }

}
