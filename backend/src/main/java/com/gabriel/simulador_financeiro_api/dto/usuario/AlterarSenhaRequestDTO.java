package com.gabriel.simulador_financeiro_api.dto.usuario;

import jakarta.validation.constraints.NotBlank;

public record AlterarSenhaRequestDTO(@NotBlank String senhaAtual, @NotBlank String senhaNova) {
    
}
