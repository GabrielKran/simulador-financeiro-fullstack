package com.gabriel.simulador_financeiro_api.service;

import com.gabriel.simulador_financeiro_api.repository.PlanoFinanceiroRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gabriel.simulador_financeiro_api.entity.PlanoFinanceiro;

@Service
public class PlanoFinanceiroService {

    @Autowired
    private PlanoFinanceiroRepository repository;

    public List<PlanoFinanceiro> searchAll() {
        List<PlanoFinanceiro> planoLista = repository.findAll();

        for (PlanoFinanceiro p : planoLista) {
            int meses = timeCalc(p);
            p.setMesesEstimados(meses);
        }

        return planoLista;
    }

    public PlanoFinanceiro save(PlanoFinanceiro plano) {
        return repository.save(plano);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public PlanoFinanceiro edit(Long id, PlanoFinanceiro plano) {
        plano.setId(id);
        return repository.save(plano);
    }

    public int timeCalc(PlanoFinanceiro plano) {
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