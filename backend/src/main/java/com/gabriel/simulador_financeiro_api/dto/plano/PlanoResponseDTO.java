package com.gabriel.simulador_financeiro_api.dto.plano;

import java.util.UUID;

import com.gabriel.simulador_financeiro_api.entity.PlanoFinanceiro;

public record PlanoResponseDTO(UUID id, String nomePlano, double metaValor, double aporteMensal, double taxaJurosAnual, Integer mesesEstimados) {
    
    public PlanoResponseDTO(PlanoFinanceiro plano) {
        this(
            plano.getId(),
            plano.getNomePlano(),
            plano.getMetaValor(),
            plano.getAporteMensal(),
            plano.getTaxaJurosAnual(),
            plano.getMesesEstimados()
        );
    }
}
