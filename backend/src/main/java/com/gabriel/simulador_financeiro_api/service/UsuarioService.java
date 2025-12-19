package com.gabriel.simulador_financeiro_api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gabriel.simulador_financeiro_api.entity.Usuario;
import com.gabriel.simulador_financeiro_api.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired PasswordEncoder passwordEncoder;

    public void editNome(String nomeNovo, Usuario usuario) {
        usuario.setNome(nomeNovo);

        repository.save(usuario);
        
    }

    public void editSenha(String senhaAtual, String senhaNova, Usuario usuario) {
        if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Senha atual incorreta");
        }

        String senhaHash = passwordEncoder.encode(senhaNova);
        usuario.setSenha(senhaHash);

        repository.save(usuario);
    }

    public void deleteUsuario(Usuario usuario, String senha) {
        Usuario usuarioDoBanco = repository.findById(usuario.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario nao encontrado"));

        if (!passwordEncoder.matches(senha, usuarioDoBanco.getSenha())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado");
        }

        repository.delete(usuarioDoBanco);

    }
}
