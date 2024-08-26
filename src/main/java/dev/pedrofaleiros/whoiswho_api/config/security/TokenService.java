package dev.pedrofaleiros.whoiswho_api.config.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import dev.pedrofaleiros.whoiswho_api.entity.UserEntity;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    private String ISSUER = "who-is-who-api";

    public String generateToken(UserEntity user) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String token = JWT.create().withIssuer(ISSUER).withSubject(user.getUsername())
                .withExpiresAt(generateExpirationDate()).sign(algorithm);
        return token;
    }

    public String validateToken(String token) {
        if (token == null) {
            return null;
        }
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.require(algorithm).withIssuer(ISSUER).build().verify(token).getSubject();
    }

    private Instant generateExpirationDate() {
        //TODO: validade token
        // return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
        return LocalDateTime.now().plusDays(7).toInstant(ZoneOffset.of("-03:00"));
    }
}
