package com.gabriel.simulador_financeiro_api.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gabriel.simulador_financeiro_api.entity.PlanoFinanceiro;
import com.gabriel.simulador_financeiro_api.entity.Usuario;

@Repository
public interface PlanoFinanceiroRepository extends JpaRepository<PlanoFinanceiro, UUID>{

    List<PlanoFinanceiro> findByUsuario(Usuario usuario);

    Optional<PlanoFinanceiro> findByIdAndUsuario(UUID idPlano, Usuario usuario);
}
