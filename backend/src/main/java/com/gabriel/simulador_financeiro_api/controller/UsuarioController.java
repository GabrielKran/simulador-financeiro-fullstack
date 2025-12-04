package com.gabriel.simulador_financeiro_api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabriel.simulador_financeiro_api.entity.Usuario;
import com.gabriel.simulador_financeiro_api.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    public List<Usuario> getUsuario() {
        return service.searchAll();
    }
    
    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable UUID id) {
        service.delete(id);
    }

    @PutMapping("/{id}")
    public Usuario putUsuario(@PathVariable UUID id, @RequestBody Usuario usuario) {
        return service.edit(id, usuario);
    }
}