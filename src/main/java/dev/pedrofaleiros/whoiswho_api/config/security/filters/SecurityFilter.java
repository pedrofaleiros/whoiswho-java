package dev.pedrofaleiros.whoiswho_api.config.security.filters;

import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import dev.pedrofaleiros.whoiswho_api.config.security.TokenService;
import dev.pedrofaleiros.whoiswho_api.exception.not_found.CustomEntityNotFoundException;
import dev.pedrofaleiros.whoiswho_api.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    TokenService tokenService;
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        var token = recoverToken(request);
        var login = tokenService.validateToken(token);

        if (login != null) {
            var user = userRepository.findByUsername(login)
                    .orElseThrow(() -> new CustomEntityNotFoundException("Usuario nao encontrado"));
            var authorities = user.getAuthorities();
            var auth = new UsernamePasswordAuthenticationToken(user, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null)
            return null;
        return authHeader.replace("Bearer ", "");
    }
}
