package com.gabriel.simulador_financeiro_api.dto;

import java.util.UUID;

import com.gabriel.simulador_financeiro_api.entity.PlanoFinanceiro;

public record PlanoResponseDTO(UUID id, String nomePlano, double metaValor, Integer mesesEstimados) {
    
    public PlanoResponseDTO(PlanoFinanceiro plano) {
        this(
            plano.getId(),
            plano.getNomePlano(),
            plano.getMetaValor(),
            plano.getMesesEstimados()
        );
    }
}