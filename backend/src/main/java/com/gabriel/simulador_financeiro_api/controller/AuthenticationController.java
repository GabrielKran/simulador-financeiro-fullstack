package com.gabriel.simulador_financeiro_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabriel.simulador_financeiro_api.dto.auth.AuthenticationDTO;
import com.gabriel.simulador_financeiro_api.dto.auth.LoginResponseDTO;
import com.gabriel.simulador_financeiro_api.dto.auth.RegisterRequestDTO;
import com.gabriel.simulador_financeiro_api.entity.Usuario;
import com.gabriel.simulador_financeiro_api.infra.security.TokenService;
import com.gabriel.simulador_financeiro_api.service.AuthorizationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


// definimos o caminho para acessar na web
@RestController
@RequestMapping("/auth")

@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {
    
    private final AuthenticationManager authManager;
    private final TokenService tokenService;
    private final AuthorizationService authorizationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> postLogin(@RequestBody @Valid AuthenticationDTO data) {
        log.info("API: Tentativa de login para o email: {}", data.email());

        var authToken = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        var auth = authManager.authenticate(authToken);

        var usuario = (Usuario) auth.getPrincipal();
        log.info("Autenticacao bem sucedida para usuario ID {}", usuario.getId());

        var token = tokenService.generateToken(data.email());

        return ResponseEntity.ok(new LoginResponseDTO(token, usuario.getNome()));
    }


    @PostMapping("/register")
    public ResponseEntity<Void> postRegister(@RequestBody @Valid RegisterRequestDTO data) {
        log.info("API: Recebida solicitacao de registro para novo usuario: {}", data.email());
        
        this.authorizationService.register(data.nome(), data.email(), data.senha());

        log.info("Registro finalizado com sucesso.");
        
        return ResponseEntity.noContent().build();
    }
    

}
