package com.gabriel.simulador_financeiro_api.entity;

import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "Plano_financeiro")

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class PlanoFinanceiro {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition="char(36)")
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name="usuario_id", nullable=false)
    private Usuario usuario;
    
    @Column(nullable=false)
    private String nomePlano;

    @Column(columnDefinition="decimal(15, 2)", nullable=false)
    private double metaValor;

    @Column(columnDefinition="decimal(15, 2)", nullable=false)
    private double aporteMensal;

    @Column(columnDefinition="decimal(5, 2)", nullable=false)
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
}