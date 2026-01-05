package com.gabriel.simulador_financeiro_api.service;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gabriel.simulador_financeiro_api.entity.PlanoFinanceiro;
import com.gabriel.simulador_financeiro_api.entity.Usuario;
import com.gabriel.simulador_financeiro_api.repository.PlanoFinanceiroRepository;

@ExtendWith(MockitoExtension.class)
public class PlanoFinanceiroServiceTeste {
    @Mock
    private PlanoFinanceiroRepository repository;

    @InjectMocks
    private PlanoFinanceiroService service;

    @Test
    @DisplayName("Deve salvar plano com sucesso")
    public void salvarPlanoFinanceiro() {

        Usuario usuarioFake = new Usuario("Teste", "teste@gmail.com", "123");
        usuarioFake.setId(UUID.randomUUID());

        PlanoFinanceiro planoRequest = new PlanoFinanceiro("Carro", );
    }
}
