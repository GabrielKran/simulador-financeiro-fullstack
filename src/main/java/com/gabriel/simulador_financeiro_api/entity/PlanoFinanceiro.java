package com.gabriel.simulador_financeiro_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Plano_financeiro")
public class PlanoFinanceiro {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column(nullable=false)
    private long usuarioId;
    
    @Column(nullable=false)
    private String nomePlano;

    @Column(nullable=false)
    private double metaValor;

    @Column(nullable=false)
    private double aporteMensal;

    @Column(nullable=false)
    private double taxaJurosAnual;

    public PlanoFinanceiro(String nomePlano, double metaValor, double aporteMensal, double taxaJurosAnual) {
        this.nomePlano = nomePlano;
        this.metaValor = metaValor;
        this.aporteMensal = aporteMensal;
        this.taxaJurosAnual = taxaJurosAnual;
    }

    public PlanoFinanceiro() {
        
    }

    public long getId() {
        return id;
    }

    public long getUsuarioId() {
        return usuarioId;
    }

    public String getNomePlano() {
        return nomePlano;
    }

    public double getMetaValor() {
        return metaValor;
    }

    public double getAporteMensal() {
        return aporteMensal;
    }

    public double getTaxaJurosAnual() {
        return taxaJurosAnual;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUsuarioId(long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setNomePlano(String nomePlano) {
        this.nomePlano = nomePlano;
    }

    public void setMetaValor(double metaValor) {
        this.metaValor = metaValor;
    }

    public void setAporteMensal(double aporteMensal) {
        this.aporteMensal = aporteMensal;
    }

    public void setTaxaJurosAnual(double taxaJurosAnual) {
        this.taxaJurosAnual = taxaJurosAnual;
    }
}