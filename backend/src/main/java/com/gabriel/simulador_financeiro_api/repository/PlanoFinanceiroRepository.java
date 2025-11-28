package com.gabriel.simulador_financeiro_api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gabriel.simulador_financeiro_api.entity.PlanoFinanceiro;

@Repository
public interface PlanoFinanceiroRepository extends JpaRepository<PlanoFinanceiro, UUID>{
    
}
