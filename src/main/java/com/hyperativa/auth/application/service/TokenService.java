package com.hyperativa.auth.application.service;

import com.auth0.jwt.algorithms.Algorithm;
import com.hyperativa.handler.exceptions.APIException;
import com.hyperativa.user.domain.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.auth0.jwt.JWT;

@Service
@Log4j2
public class TokenService {
    @Value("${env.secret}")
    private String secret;
    private static final String ISSUER = "Lirou Store";

    public String generateToken(User user) {
        try {
            Algorithm algoritimo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(user.getUsername())
                    .withExpiresAt(LocalDateTime.now().plusDays(7).toInstant(ZoneOffset.of("-03:00")))
                    .withClaim("ROLE", user.getRole().toString())
                    .sign(algoritimo);
        }
        catch (Exception exception) {
            log.error(exception.getMessage());
            throw new APIException("Erro ao gerar JWT", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String decode(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        token = token.replace("Bearer ", "");
        try {
            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new APIException("Token inválido ou expirado!", HttpStatus.CONFLICT);
        }
    }
}
