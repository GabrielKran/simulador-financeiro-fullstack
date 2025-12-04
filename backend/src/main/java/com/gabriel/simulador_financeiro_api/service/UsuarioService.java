package com.gabriel.simulador_financeiro_api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gabriel.simulador_financeiro_api.entity.Usuario;
import com.gabriel.simulador_financeiro_api.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    public List<Usuario> searchAll() {
        return repository.findAll();
    }

    public Usuario save(Usuario usuario) {
        return repository.save(usuario);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Usuario edit(UUID id, Usuario usuario) {
        usuario.setId(id);
        return repository.save(usuario);
    }
}
