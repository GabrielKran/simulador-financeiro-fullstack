package com.gabriel.simulador_financeiro_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
@Entity
@Table(name = "Plano_financeiro")
public class PlanoFinanceiro {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @ManyToOne
    @JoinColumn(name="usuario_id", nullable=false)
    private Usuario usuario;
    
    @Column(nullable=false)
    private String nomePlano;

    @Column(nullable=false)
    private double metaValor;

    @Column(nullable=false)
    private double aporteMensal;

    @Column(nullable=false)
    private double taxaJurosAnual;

    @Transient
    private Integer mesesEstimados;

    public PlanoFinanceiro(Usuario usuario, String nomePlano, double metaValor, double aporteMensal, double taxaJurosAnual) {
        this.usuario = usuario;
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

    public Usuario getUsuario() {
        return usuario;
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

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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

    public Integer getMesesEstimados() {
        return mesesEstimados;
    }

    public void setMesesEstimados(Integer mesesEstimados) {
        this.mesesEstimados = mesesEstimados;
    }
}