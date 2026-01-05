package com.gabriel.simulador_financeiro_api.infra.security;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(String email) {
        try {
            
            // tipo de algotirimo para o token JWT
            Algorithm algorithm = Algorithm.HMAC256(secret);

            // criando o token JWT
            String token = JWT.create()

            // definindo o nome (identificador) do JWT
            .withIssuer("simulador-financeiro-api")

            // garante que o token seja válido apenas para o usuario que pediu atraves do email
            .withSubject(email)

            // definindo o tempo de expiração do token (20 minutos)
            .withExpiresAt(genExpirationDate())

            // por fim assinamos colocando o tipo de algoritmo 
            .sign(algorithm);

            log.info("Token JWT gerado com sucesso para: {}", email);
            return token;

        } catch (JWTCreationException exception) {
            log.error("Erro critico ao gerar token JWT para email {}", email, exception);
            throw new RuntimeException("Erro ao criptografar com JWT", exception);
        }
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusMinutes(20).toInstant(ZoneOffset.of("-03:00"));
    }

    public String validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        try {
            
            return JWT.require(algorithm)
            .withIssuer("simulador-financeiro-api")
            .build()
            .verify(token)
            .getSubject();

        } catch (JWTVerificationException error) {
            log.warn("Falha na validacao do token: {}", error.getMessage());
            return null;
        }
    }
}
