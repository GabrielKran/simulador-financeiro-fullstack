package com.gabriel.simulador_financeiro_api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.gabriel.simulador_financeiro_api.entity.PlanoFinanceiro;
import com.gabriel.simulador_financeiro_api.entity.Usuario;
import com.gabriel.simulador_financeiro_api.repository.PlanoFinanceiroRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j // Habilita os logs
@RequiredArgsConstructor // Injeção de dependência segura (substitui Autowired)
public class PlanoFinanceiroService {

    // 'final' garante que nunca serão nulos e não podem ser trocados
    private final PlanoFinanceiroRepository repository;

    public List<PlanoFinanceiro> searchByUsuario(Usuario usuario) {
        log.info("Buscando planos para o usuario ID {}", usuario.getId());

        List<PlanoFinanceiro> planos = repository.findByUsuario(usuario);

        for (PlanoFinanceiro plano : planos) {
            int meses = timeCalc(plano);
            plano.setMesesEstimados(meses);
        }

        log.info("Encontrados {} planos para o usuario ID {}", planos.size(), usuario.getId());
        return planos;
    }
    
    public PlanoFinanceiro searchByIdAndUsuario(UUID idPlano, Usuario usuario) {
        log.info("Buscando detalhes do plano ID {}", idPlano);
        
        PlanoFinanceiro plano = repository.findByIdAndUsuario(idPlano, usuario).orElse(null);

        if (plano != null) {
            int meses = timeCalc(plano);
            plano.setMesesEstimados(meses);
            return plano;
            
        } else {
            log.warn("Plano ID {} nao encontrado ou nao pertence ao usuario ID {}", idPlano, usuario.getId());
            return null;
        }
    }

    public PlanoFinanceiro save(PlanoFinanceiro plano, Usuario usuario) {
        log.info("Criando novo plano '{}' (Meta: R$ {}) para usuario ID {}", plano.getNomePlano(), plano.getMetaValor(), usuario.getId());

        plano.setUsuario(usuario);
        PlanoFinanceiro salvo = repository.save(plano);

        log.info("Plano criado com sucesso. ID gerado: {}", salvo.getId());
        return salvo;
    }

    public void delete(UUID idPlano, Usuario usuario) {
        log.info("Solicitacao de exclusao do plano ID {}", idPlano);

        PlanoFinanceiro plano = repository.findByIdAndUsuario(idPlano, usuario)
            .orElseThrow(() ->  {
                log.warn("Tentativa de deletar plano falhou: ID {} nao encontrado para este usuario", idPlano);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Plano nao encontrado");
            });

        repository.delete(plano);
        log.info("Plano ID {} excluido com sucesso", idPlano);
    }

    public PlanoFinanceiro edit(UUID idPlano, PlanoFinanceiro planoDados, Usuario usuario) {
        log.info("Editando plano ID {}", idPlano);

        PlanoFinanceiro plano = repository.findByIdAndUsuario(idPlano, usuario)
            .orElseThrow(() -> {
                log.error("Erro ao editar: Plano ID {} nao encontrado", idPlano);    
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Plano nao encontrado");
            });
        
        plano.setNomePlano(planoDados.getNomePlano());
        plano.setMetaValor(planoDados.getMetaValor());
        plano.setAporteMensal(planoDados.getAporteMensal());
        plano.setTaxaJurosAnual(planoDados.getTaxaJurosAnual());

        PlanoFinanceiro atualizado = repository.save(plano);
        log.info("Plano ID {} atualizado com sucesso", idPlano);

        return atualizado;
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
                log.warn("Plano ID {} atingiu limite de 1000 meses no calculo", plano.getId());
                break;
            }
        }
        return contadorMeses;
    }
}