package com.gabriel.simulador_financeiro_api.infra.security;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(String email) {

        try {
            
            // tipo de algotirimo para o token JWT
            Algorithm algorithm = Algorithm.HMAC256(secret);

            // criando o token JWT
            return JWT.create()

            // definindo o nome (identificador) do JWT
            .withIssuer("simulador-financeiro-api")

            // garante que o token seja válido apenas para o usuario que pediu atraves do email
            .withSubject(email)

            // definindo o tempo de expiração do token (2 horas)
            .withExpiresAt(genExpirationDate())

            // por fim assinamos colocando o tipo de algoritmo 
            .sign(algorithm);

        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao criptografar com JWT", exception);
        }
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
