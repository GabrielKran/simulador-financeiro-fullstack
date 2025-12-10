package com.gabriel.simulador_financeiro_api.dto;

import com.gabriel.simulador_financeiro_api.entity.PlanoFinanceiro;

public record PlanoResponseDetailsDTO(String nomePlano, double metaValor, double aporteMensal, double taxaJurosAnual, Integer mesesEstimados) {
    
    public PlanoResponseDetailsDTO(PlanoFinanceiro plano) {
        this(
            plano.getNomePlano(),
            plano.getMetaValor(),
            plano.getAporteMensal(),
            plano.getTaxaJurosAnual(),
            plano.getMesesEstimados()
        );
    }
}
