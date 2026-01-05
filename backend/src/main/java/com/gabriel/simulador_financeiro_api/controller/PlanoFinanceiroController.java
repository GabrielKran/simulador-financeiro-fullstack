package com.gabriel.simulador_financeiro_api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabriel.simulador_financeiro_api.dto.plano.PlanoRequestDTO;
import com.gabriel.simulador_financeiro_api.dto.plano.PlanoResponseDTO;
import com.gabriel.simulador_financeiro_api.entity.PlanoFinanceiro;
import com.gabriel.simulador_financeiro_api.entity.Usuario;
import com.gabriel.simulador_financeiro_api.service.PlanoFinanceiroService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/planos-financeiros")

@Slf4j
@RequiredArgsConstructor
public class PlanoFinanceiroController {

    private final PlanoFinanceiroService service;

    private Usuario getUsuarioLogado() {
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @GetMapping
    public ResponseEntity<List<PlanoResponseDTO>> getPlanoFinanceiro() {
        Usuario usuario = getUsuarioLogado();
        log.info("API: Buscando todos os planos do usuario ID {}", usuario.getId());

        List<PlanoFinanceiro> planoLista = service.searchByUsuario(usuario);

        List<PlanoResponseDTO> planoListaDTO = planoLista.stream().map(
            plano -> new PlanoResponseDTO(plano))
            .toList();

        return ResponseEntity.ok(planoListaDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanoResponseDTO> getPlanoById(@PathVariable UUID id) {
        Usuario usuario = getUsuarioLogado();
        log.info("API: Buscando detalhes do plano ID {} para usuario ID {}", id, usuario.getId());

        PlanoFinanceiro plano = service.searchByIdAndUsuario(id, usuario);
        
        if (plano != null) {
            return ResponseEntity.ok(new PlanoResponseDTO(plano));

        } else {
            return ResponseEntity.notFound().build();
        }
    }
    

    @PostMapping
    public ResponseEntity<PlanoResponseDTO> postPlanoFinanceiro(@RequestBody @Valid PlanoRequestDTO data) {
        Usuario usuario = getUsuarioLogado();
        log.info("API: Recebida solicitacao de CRIACAO de plano para usuario ID {}", usuario.getId());
        
        PlanoFinanceiro novoPlano = new PlanoFinanceiro();

        novoPlano.setNomePlano(data.nomePlano());
        novoPlano.setMetaValor(data.metaValor());
        novoPlano.setAporteMensal(data.aporteMensal());
        novoPlano.setTaxaJurosAnual(data.taxaJurosAnual());

        PlanoFinanceiro plano = service.save(novoPlano, usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(new PlanoResponseDTO(plano));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlanoFinanceiro(@PathVariable UUID id) {
        Usuario usuario = getUsuarioLogado();
        log.info("API: Recebida solicitacao de EXCLUSAO do plano ID {}", id);

        service.delete(id, usuario);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanoResponseDTO> putPlanoFinanceiro(@PathVariable UUID id, @RequestBody @Valid PlanoRequestDTO data) {
        Usuario usuario = getUsuarioLogado();
        log.info("API: Recebida solicitacao de EDICAO do plano ID {}", id);
        
        PlanoFinanceiro novoPlano = new PlanoFinanceiro();

        novoPlano.setNomePlano(data.nomePlano());
        novoPlano.setMetaValor(data.metaValor());
        novoPlano.setAporteMensal(data.aporteMensal());
        novoPlano.setTaxaJurosAnual(data.taxaJurosAnual());

        PlanoFinanceiro plano = service.edit(id, novoPlano, usuario);

        return ResponseEntity.ok(new PlanoResponseDTO(plano));
    }
}
