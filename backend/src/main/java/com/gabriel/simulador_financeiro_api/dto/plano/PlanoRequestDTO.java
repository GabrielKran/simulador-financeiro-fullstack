package com.gabriel.simulador_financeiro_api.dto.plano;

public record PlanoRequestDTO(String nomePlano, double metaValor, double aporteMensal, double taxaJurosAnual) {
}
