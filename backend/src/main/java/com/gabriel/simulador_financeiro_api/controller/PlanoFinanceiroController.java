package com.gabriel.simulador_financeiro_api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.gabriel.simulador_financeiro_api.dto.PlanoRequestDTO;
import com.gabriel.simulador_financeiro_api.dto.PlanoResponseDTO;
import com.gabriel.simulador_financeiro_api.dto.PlanoResponseDetailsDTO;
import com.gabriel.simulador_financeiro_api.entity.PlanoFinanceiro;
import com.gabriel.simulador_financeiro_api.entity.Usuario;
import com.gabriel.simulador_financeiro_api.repository.UsuarioRepository;
import com.gabriel.simulador_financeiro_api.service.PlanoFinanceiroService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/planos-financeiros")
public class PlanoFinanceiroController {

    @Autowired
    private PlanoFinanceiroService service;

    @Autowired
    private UsuarioRepository repository;

    private Usuario getUsuarioLogado() {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();

        return repository.findByEmail(email);
    }

    @GetMapping
    public ResponseEntity<List<PlanoResponseDTO>> getPlanoFinanceiro() {
        List<PlanoFinanceiro> planoLista = service.searchByUsuario(getUsuarioLogado());

        List<PlanoResponseDTO> planoListaDTO = planoLista.stream().map(
            plano -> new PlanoResponseDTO(plano))
            .toList();

        return ResponseEntity.ok(planoListaDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanoResponseDetailsDTO> getPlanoById(@PathVariable UUID id) {
        PlanoFinanceiro plano = service.searchByIdAndUsuario(id, getUsuarioLogado());
        
        if (plano != null) {
            return ResponseEntity.ok(new PlanoResponseDetailsDTO(plano));

        } else {
            return ResponseEntity.notFound().build();
        }
    }
    

    @PostMapping
    public ResponseEntity<PlanoResponseDTO> postPlanoFinanceiro(@RequestBody @Valid PlanoRequestDTO data) {
        PlanoFinanceiro novoPlano = new PlanoFinanceiro();

        novoPlano.setNomePlano(data.nomePlano());
        novoPlano.setMetaValor(data.metaValor());
        novoPlano.setAporteMensal(data.aporteMensal());
        novoPlano.setTaxaJurosAnual(data.taxaJurosAnual());

        PlanoFinanceiro plano = service.save(novoPlano, getUsuarioLogado());

        return ResponseEntity.ok(new PlanoResponseDTO(plano));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlanoFinanceiro(@PathVariable UUID id) {
        
        service.delete(id, getUsuarioLogado());

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanoResponseDTO> putPlanoFinanceiro(@PathVariable UUID id, @RequestBody PlanoRequestDTO data) {
        PlanoFinanceiro novoPlano = new PlanoFinanceiro();

        novoPlano.setNomePlano(data.nomePlano());
        novoPlano.setMetaValor(data.metaValor());
        novoPlano.setAporteMensal(data.aporteMensal());
        novoPlano.setTaxaJurosAnual(data.taxaJurosAnual());

        PlanoFinanceiro plano = service.edit(id, novoPlano, getUsuarioLogado());

        return ResponseEntity.ok(new PlanoResponseDTO(plano));
    }
}
