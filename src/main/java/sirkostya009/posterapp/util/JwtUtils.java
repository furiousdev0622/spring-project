package sirkostya009.posterapp.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

    private final byte[] SECRET_KEY = "SUPER_SECRET_KEY_THAT_YOU_SHALL_NEVER_FIND_OUT_HAHAHA".getBytes();
    public final String Bearer = "Bearer ";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        var claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails details) {
        return createToken(new HashMap<>(), details.getUsername());
    }

    private Date expirationDate() {
        return new Date(System.currentTimeMillis() + (long) 1e+8);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject).setIssuedAt(new Date())
                .setExpiration(expirationDate())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY)).compact();
    }

    public boolean validateToken(String token, UserDetails details) {
        var username = extractUsername(token);
        return username.equals(details.getUsername()) && !isTokenExpired(token);
    }

}
