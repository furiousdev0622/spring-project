package sirkostya009.posterapp.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import sirkostya009.posterapp.model.AppUser;

public class UserUtils {
    public static AppUser userFromToken(UsernamePasswordAuthenticationToken token) {
        return (AppUser) token.getPrincipal();
    }
}
