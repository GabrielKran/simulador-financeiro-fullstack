package com.gabriel.simulador_financeiro_api.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthenticationDTO(
    
    @NotBlank
    @Email
    String email,

    @NotBlank
    @Size(min = 6)
    String senha) {}
