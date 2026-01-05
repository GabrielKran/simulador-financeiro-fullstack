package com.gabriel.simulador_financeiro_api.controller;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/usuarios")

@Slf4j
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;

    private Usuario getUsuarioLogado() {
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PatchMapping("/me/nome")
    public ResponseEntity<AlterarNomeResponseDTO> patchNome(@RequestBody @Valid AlterarNomeRequestDTO data) {
        Usuario usuario = getUsuarioLogado();
        log.info("API: Recebida solicitacao de troca de nome para usuario ID {}", usuario.getId());
        
        service.editNome(data.nomeNovo(), usuario);

        return ResponseEntity.ok(new AlterarNomeResponseDTO(data.nomeNovo()));
    }

    @PatchMapping("/me/senha")
    public ResponseEntity<Void> patchSenha(@RequestBody @Valid AlterarSenhaRequestDTO data) {
        Usuario usuario = getUsuarioLogado();
        log.info("API: Recebida solicitacao de troca de senha para usuario ID {}", usuario.getId());

        service.editSenha(data.senhaAtual(), data.senhaNova(), usuario);

        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUsuario(@RequestBody @Valid DeletarUsuarioRequestDTO data) {
        Usuario usuario = getUsuarioLogado();
        log.info("API: Recebida solicitacao de EXCLUSAO DE CONTA para usuario ID {}", usuario.getId());

        service.deleteUsuario(usuario, data.senha());

        return ResponseEntity.noContent().build();
    }
}