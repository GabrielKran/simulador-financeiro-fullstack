package com.gabriel.simulador_financeiro_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabriel.simulador_financeiro_api.entity.PlanoFinanceiro;
import com.gabriel.simulador_financeiro_api.service.PlanoFinanceiroService;

@RestController
@RequestMapping("/planos-financeiros")
@CrossOrigin(origins = "*")
public class PlanoFinanceiroController {

    @Autowired
    private PlanoFinanceiroService service;

    @GetMapping
    public List<PlanoFinanceiro> getPlanoFinanceiro() {
        return service.searchAll();
    }

    @PostMapping
    public PlanoFinanceiro postPlanoFinanceiro(@RequestBody PlanoFinanceiro planoFinanceiro) {
        return service.save(planoFinanceiro);
    }

    @DeleteMapping("/{id}")
    public void deletePlanoFinanceiro(@PathVariable Long id) {
        service.delete(id);
    }

    @PutMapping("/{id}")
    public PlanoFinanceiro putPlanoFinanceiro(@PathVariable Long id, @RequestBody PlanoFinanceiro planoFinanceiro) {
        return service.edit(id, planoFinanceiro);
    }
}
