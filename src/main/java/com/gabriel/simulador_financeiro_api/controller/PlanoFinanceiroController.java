package com.gabriel.simulador_financeiro_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabriel.simulador_financeiro_api.entity.PlanoFinanceiro;
import com.gabriel.simulador_financeiro_api.repository.PlanoFinanceiroRepository;
@RestController
@RequestMapping("/planos-financeiros")
@CrossOrigin(origins = "*")
public class PlanoFinanceiroController {
    @Autowired
    private PlanoFinanceiroRepository repository;

    @GetMapping
    public List<PlanoFinanceiro> getPlanoFinanceiro() {
        return repository.findAll();
    }

    @PostMapping
    public PlanoFinanceiro postPlanoFinanceiro(@RequestBody PlanoFinanceiro planoFinanceiro) {
        return repository.save(planoFinanceiro);
    }
}
