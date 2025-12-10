package com.gabriel.simulador_financeiro_api.dto;

public record PlanoRequestDTO(String nomePlano, double metaValor, double aporteMensal, double taxaJurosAnual) {
}
