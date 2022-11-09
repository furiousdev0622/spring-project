package sirkostya009.posterapp.security;

import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class SecretKeyConfig {

    private final static byte[] KEY = "SUPER_SECRET_KEY_THAT_YOU_SHALL_NEVER_FIND_OUT_HAHAHA".getBytes(); // this is better put somewhere more safe

    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(KEY);
    }

}
