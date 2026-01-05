package com.gabriel.simulador_financeiro_api.dto.plano;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PlanoRequestDTO(
     
    @NotBlank
    String nomePlano,

    @NotNull
    @Positive
    double metaValor,

    @NotNull
    @Positive
    double aporteMensal,

    @NotNull
    @Positive
    double taxaJurosAnual) {}