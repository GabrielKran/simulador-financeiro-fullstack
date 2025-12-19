package com.gabriel.simulador_financeiro_api.entity;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Usuario")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition="char(36)")
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;

    @Column(nullable=false)
    private String nome;

    @Column(nullable=false, unique=true)
    private String email;

    @Column(nullable=false)
    private String senha;

    @OneToMany(mappedBy="usuario", cascade=CascadeType.ALL, orphanRemoval=true)
    @JsonIgnore
    private List<PlanoFinanceiro> planos;

    public Usuario(){

    }

    public Usuario(String nome, String email, String senha) {
        this.nome= nome;
        this.email = email;
        this.senha = senha;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<PlanoFinanceiro> getPlanos() {
        return planos;
    }

    public void setPlanos(List<PlanoFinanceiro> planos) {
        this.planos = planos;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return id != null && id.equals(usuario.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}