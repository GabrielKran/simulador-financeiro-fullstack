package com.gabriel.simulador_financeiro_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gabriel.simulador_financeiro_api.entity.Usuario;
import com.gabriel.simulador_financeiro_api.repository.UsuarioRepository;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Busca o usuário no banco (Objeto "sujo" com conexões JPA)
        var usuarioDoBanco = repository.findByEmail(username);
        
        if (usuarioDoBanco == null) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        // 2. Cria um objeto "limpo" do Spring Security (Sanitização)
        // Estamos copiando apenas Email, Senha e Autorizações.
        // O resto (Planos, ID, etc) fica para trás. O Loop morre aqui.
        return new User(
            usuarioDoBanco.getUsername(), // Email
            usuarioDoBanco.getPassword(), // Senha
            usuarioDoBanco.getAuthorities() // Perfil (ROLE_USER)
        );
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(String nome, String email, String senha) {
        if (repository.existsByEmail(email)) {

            throw new RuntimeException("Email já existe");

        } else {
            String senhaBCrypt = passwordEncoder.encode(senha);

            Usuario usuario = new Usuario(nome, email, senhaBCrypt);
            
            repository.save(usuario);
        }
    }
}