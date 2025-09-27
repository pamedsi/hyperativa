package com.hyperativa.auth.application.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import lombok.extern.log4j.Log4j2;

import com.hyperativa.handler.exceptions.JwtException;
import com.hyperativa.user.domain.User;

@Service
@Log4j2
public class TokenService {
    @Value("${env.secret}")
    private String secret;
    @Value("${env.issuer}")
    private String issuer;

    public String generateToken(User user) {
        try {
            Algorithm algoritimo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(user.getUsername())
                    .withExpiresAt(LocalDateTime.now().plusMinutes(15).toInstant(ZoneOffset.of("-03:00")))
                    .withClaim("ROLE", user.getRole().toString())
                    .sign(algoritimo);
        }
        catch (Exception exception) {
            log.error(exception.getMessage());
            throw new JwtException("Error generating JWT");
        }
    }

    public String decode(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        token = token.replace("Bearer ", "");
        try {
            return JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new JwtException("Invalid or expired token!");
        }
    }

}
