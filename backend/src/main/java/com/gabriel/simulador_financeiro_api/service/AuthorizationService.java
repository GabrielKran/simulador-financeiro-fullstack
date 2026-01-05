package com.gabriel.simulador_financeiro_api.service;


import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gabriel.simulador_financeiro_api.entity.Usuario;
import com.gabriel.simulador_financeiro_api.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j // Habilita os logs
@RequiredArgsConstructor // Injeção de dependência segura (substitui Autowired)
public class AuthorizationService implements UserDetailsService {

    // 'final' garante que nunca serão nulos e não podem ser trocados
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Tentativa de login para o usuario: {}", username);
        
        Usuario usuarioDoBanco = repository.findByEmail(username).orElseThrow(() -> {
            log.warn("Falha no login: Usuario '{}' nao encontrado no banco", username);
            return new UsernameNotFoundException("Usuário não encontrado");
        });

        return usuarioDoBanco;
    }


    public void register(String nome, String email, String senha) {
        log.info("Tentativa de registro de novo usuario: {}", email);

        if (repository.existsByEmail(email)) {
            log.warn("Falha no registro: Email '{}' ja cadastrado no sistema", email);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ja existe");

        }
            String senhaBCrypt = passwordEncoder.encode(senha);

            Usuario usuario = new Usuario(nome, email, senhaBCrypt);
            
            Usuario salvo = repository.save(usuario);
            log.info("Usuario registrado com sucesso! ID: {}", salvo.getId());
    }
}