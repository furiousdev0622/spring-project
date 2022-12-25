package sirkostya009.posterapp.security;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class SecretKeyConfig {

    @Value("${secret_key}")
    private String keyString;

    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(keyString.getBytes());
    }

}
