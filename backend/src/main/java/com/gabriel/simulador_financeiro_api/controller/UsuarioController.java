package com.gabriel.simulador_financeiro_api.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabriel.simulador_financeiro_api.dto.usuario.AlterarNomeRequestDTO;
import com.gabriel.simulador_financeiro_api.dto.usuario.AlterarNomeResponseDTO;
import com.gabriel.simulador_financeiro_api.dto.usuario.AlterarSenhaRequestDTO;
import com.gabriel.simulador_financeiro_api.dto.usuario.DeletarUsuarioRequestDTO;
import com.gabriel.simulador_financeiro_api.entity.Usuario;
import com.gabriel.simulador_financeiro_api.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    private Usuario getUsuarioLogado() {
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PatchMapping("/me/nome")
    public ResponseEntity<AlterarNomeResponseDTO> patchNome(@RequestBody @Valid AlterarNomeRequestDTO data) {
        service.editNome(data.nomeNovo(), getUsuarioLogado());

        return ResponseEntity.ok(new AlterarNomeResponseDTO(data.nomeNovo()));
    }

    @PatchMapping("/me/senha")
    public ResponseEntity<Void> patchSenha(@RequestBody @Valid AlterarSenhaRequestDTO data) {
        service.editSenha(data.senhaAtual(), data.senhaNova(), getUsuarioLogado());

        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUsuario(@RequestBody @Valid DeletarUsuarioRequestDTO data) {
        service.deleteUsuario(getUsuarioLogado(), data.senha());

        return ResponseEntity.noContent().build();
    }
}