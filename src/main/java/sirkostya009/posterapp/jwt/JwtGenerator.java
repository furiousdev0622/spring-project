package sirkostya009.posterapp.jwt;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sirkostya009.posterapp.security.JwtConfig;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtGenerator {

    private final static long FOURTEEN_DAYS_IN_MILLISECONDS = (long) 1.21e+9;

    private final SecretKey secretKey;

    public String generate(String subject) {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setSubject(subject)
                .setIssuer(JwtConfig.ISSUER)
                .setExpiration(new Date(System.currentTimeMillis() + FOURTEEN_DAYS_IN_MILLISECONDS))
                .signWith(secretKey)
                .compact();
    }

}
