package sirkostya009.posterapp.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import sirkostya009.posterapp.service.UserService;
import sirkostya009.posterapp.util.JwtUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        var header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith(jwtUtils.Bearer)) {
            chain.doFilter(request, response);
            return;
        }

        var jwt = header.replace(jwtUtils.Bearer, "");
        var username = jwtUtils.extractUsername(jwt);

        if (username == null && SecurityContextHolder.getContext().getAuthentication() != null) {
            chain.doFilter(request, response);
            return;
        }

        var user = userService.loadUserByUsername(username);

        if (!jwtUtils.validateToken(jwt, user) || !user.isEnabled()) {
            chain.doFilter(request, response);
            return;
        }

        var passwordAuthToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        passwordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // implies that request should contain user details?
        SecurityContextHolder.getContext().setAuthentication(passwordAuthToken);
    }
}
