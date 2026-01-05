package com.gabriel.simulador_financeiro_api.dto.usuario;

import jakarta.validation.constraints.NotBlank;

public record AlterarNomeRequestDTO(
    
    @NotBlank
    String nomeNovo) {}