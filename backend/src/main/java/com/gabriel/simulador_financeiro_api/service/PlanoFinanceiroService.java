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
        return repository.findAll();
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
        return 0;
    }
}
