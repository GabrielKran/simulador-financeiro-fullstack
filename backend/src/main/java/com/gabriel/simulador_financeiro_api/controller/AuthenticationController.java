package com.gabriel.simulador_financeiro_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabriel.simulador_financeiro_api.dto.AuthenticationDTO;
import com.gabriel.simulador_financeiro_api.dto.LoginResponseDTO;
import com.gabriel.simulador_financeiro_api.dto.RegisterDTO;
import com.gabriel.simulador_financeiro_api.infra.security.TokenService;
import com.gabriel.simulador_financeiro_api.service.AuthorizationService;

import jakarta.validation.Valid;


// definimos o caminho para acessar na web
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    
    // Autowired para conseguir usar todos os metodos das classes necessarias
    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenService tokenService;

    // ResponseEntity para dar uma resposta HTTP válida, requestBody para pegar o Json e Valid para validar se é vazio
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> postLogin(@RequestBody @Valid AuthenticationDTO data) {

        // por padrao o authManager precisa deste formato, criamos ele e passamos email e senha definidos na record
        var authToken = new UsernamePasswordAuthenticationToken(data.email(), data.senha());

        // com o formato certo e os valores presentes, podemos iniciar a autenticação
        authManager.authenticate(authToken);

        // agr pedimos para gerar o token com a classe ja criada do tokenService, passando o tipo usuario
        var token = tokenService.generateToken(data.email());

        // retorna a resposta junto com o token para uso
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @Autowired
    private AuthorizationService authorizationService;

    @PostMapping("/register")
    public ResponseEntity<Void> postRegister(@RequestBody @Valid RegisterDTO data) {
        this.authorizationService.register(data.nome(), data.email(), data.senha());
        
        return ResponseEntity.ok().build();
    }
    

}
