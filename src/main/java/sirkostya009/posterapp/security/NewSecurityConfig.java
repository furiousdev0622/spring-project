//package sirkostya009.posterapp.security;
//
//import io.jsonwebtoken.security.Keys;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
//import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
//import org.springframework.security.oauth2.core.OAuth2TokenValidator;
//import org.springframework.security.oauth2.jwt.*;
//import org.springframework.security.web.SecurityFilterChain;
//import sirkostya009.posterapp.service.UserService;
//
//import java.util.List;
//
//import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
//import static org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames.SUB;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class NewSecurityConfig {
//
//    private final byte[] Key = "SUPER_SECRET_KEY_THAT_YOU_SHALL_NEVER_FIND_OUT_HAHAHA".getBytes();
//    private final String Issuer = "localhost";
//    private final UserService userService;
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        return http.authorizeRequests(authorize -> authorize
//                        .antMatchers("/api/v1/register/**", "/api/v1/users/authenticate").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .sessionManagement().sessionCreationPolicy(STATELESS)
//                .and()
//                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
//                .build();
//    }
//
//    @Bean
//    public JwtDecoder jwtDecoder() {
//        var decoder = NimbusJwtDecoder.withSecretKey(Keys.hmacShaKeyFor(Key)).build();
//        decoder.setJwtValidator(delegatingValidator());
//        return decoder;
//    }
//
//    public OAuth2TokenValidator<Jwt> delegatingValidator() {
//        return new DelegatingOAuth2TokenValidator<>(
//                List.of(
//                        subjectValidator(),
//                        new JwtTimestampValidator(),
//                        new JwtIssuerValidator(Issuer)
//                )
//        );
//    }
//
//    private OAuth2TokenValidator<Jwt> subjectValidator() {
//        return new JwtClaimValidator<String>(SUB, sub -> userService.loadUserByUsername(sub).isEnabled());
//    }
//
//}
