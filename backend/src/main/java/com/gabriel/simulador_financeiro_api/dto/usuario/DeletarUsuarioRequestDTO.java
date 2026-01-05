package com.gabriel.simulador_financeiro_api.dto.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DeletarUsuarioRequestDTO(

    @NotBlank
    @Size(min = 6)
    String senha) {}