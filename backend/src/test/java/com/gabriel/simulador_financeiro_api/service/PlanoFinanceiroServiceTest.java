package com.gabriel.simulador_financeiro_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
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
public class PlanoFinanceiroServiceTest {
    @Mock
    private PlanoFinanceiroRepository repository;

    @InjectMocks
    private PlanoFinanceiroService service;

    @Test
    @DisplayName("Deve buscar planos do usuário e CALCULAR O TEMPO corretamente")
    public void calcTimeTest() {
        Usuario usuarioTest = new Usuario("Teste", "teste@gmail.com", "123456");
        usuarioTest.setId(UUID.randomUUID());

        PlanoFinanceiro planoDoBanco = new PlanoFinanceiro(usuarioTest, "Carro", 100000, 500, 10);
        planoDoBanco.setId(UUID.randomUUID());

        when(repository.findByUsuario(usuarioTest)).thenReturn(List.of(planoDoBanco));

        List<PlanoFinanceiro> resultado = service.searchByUsuario(usuarioTest);

        assertEquals(1, resultado.size());
        assertEquals(118, resultado.get(0).getMesesEstimados());
    }
}