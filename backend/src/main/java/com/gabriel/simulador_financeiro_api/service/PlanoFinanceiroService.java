package com.gabriel.simulador_financeiro_api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gabriel.simulador_financeiro_api.entity.PlanoFinanceiro;
import com.gabriel.simulador_financeiro_api.entity.Usuario;
import com.gabriel.simulador_financeiro_api.repository.PlanoFinanceiroRepository;

@Service
public class PlanoFinanceiroService {

    @Autowired
    private PlanoFinanceiroRepository repository;

    public List<PlanoFinanceiro> searchByUsuario(Usuario usuario) {
        List<PlanoFinanceiro> planos = repository.findByUsuario(usuario);

        for (PlanoFinanceiro plano : planos) {
            int meses = timeCalc(plano);
            plano.setMesesEstimados(meses);
        }

        return planos;
    }
    
    public PlanoFinanceiro searchByIdAndUsuario(UUID idPlano, Usuario usuario) {
        PlanoFinanceiro plano = repository.findByIdAndUsuario(idPlano, usuario).orElse(null);

        if (plano != null) {
            int meses = timeCalc(plano);
            plano.setMesesEstimados(meses);
            return plano;
            
        } else {
            return null;
        }
    }

    public PlanoFinanceiro save(PlanoFinanceiro plano, Usuario usuario) {
        plano.setUsuario(usuario);
        return repository.save(plano);
    }

    public void delete(UUID idPlano, Usuario usuario) {
        PlanoFinanceiro plano = repository.findByIdAndUsuario(idPlano, usuario)
            .orElseThrow(() -> new RuntimeException("Erro ao deletar plano ou acesso negado"));

        repository.delete(plano);
    }

    public PlanoFinanceiro edit(UUID idPlano, PlanoFinanceiro planoDados, Usuario usuario) {
        PlanoFinanceiro plano = repository.findByIdAndUsuario(idPlano, usuario)
            .orElseThrow(() -> new RuntimeException("Erro ao editar plano ou acesso negado"));
        
        plano.setNomePlano(planoDados.getNomePlano());
        plano.setMetaValor(planoDados.getMetaValor());
        plano.setAporteMensal(planoDados.getAporteMensal());
        plano.setTaxaJurosAnual(planoDados.getTaxaJurosAnual());

        return repository.save(plano);
    }

    private int timeCalc(PlanoFinanceiro plano) {
        if (plano.getAporteMensal() <= 0) return 0;

        double taxaJurosMensal = (plano.getTaxaJurosAnual() / 100) / 12;
        int contadorMeses = 0;
        double saldo = 0;

        while (saldo < plano.getMetaValor()) {
            saldo += plano.getAporteMensal();
            saldo = saldo + (saldo * taxaJurosMensal);

            contadorMeses += 1;
            if (contadorMeses > 1000) {
                break;
            }
        }
        return contadorMeses;
    }
}