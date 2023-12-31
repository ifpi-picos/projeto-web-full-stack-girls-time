package com.teamg.BookBee.model;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;


@Entity
@Table(name= "leitores" )
public class Leitor implements UserDetails{
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name= "id_leitor")
    private long id;

    @Column(name= "nome")
    private String nome;

    @Column(name = "nome_usuario")
    private String nomeUsuario;

    @Column(name = "email", unique = true)
    @Email
    private String email;

    @Column(name= "senha")
    private String senha;

    @OneToMany(mappedBy = "leitor", cascade = CascadeType.ALL)
    private Set<Livro> livros;

    @OneToMany(mappedBy = "leitor")
    private Set<Anotacao> anotacoes;

    @OneToMany(mappedBy = "leitor")
    private Set<ListaDeLeitura> listaDeLeituras;

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
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

    public Set<Livro> getLivros() {
        return livros;
    }

    public void setLivros(Set<Livro> livros) {
        this.livros = livros;
    }

      public Set<Anotacao> getAnotacoes() {
        return anotacoes;
    }

    public void setAnotacoes(Set<Anotacao> anotacoes) {
        this.anotacoes = anotacoes;
    }

    public Set<ListaDeLeitura> getListaDeLeituras() {
        return listaDeLeituras;
    }

    public void setListaDeLeituras(Set<ListaDeLeitura> listaDeLeituras) {
        this.listaDeLeituras = listaDeLeituras;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.getSenha();
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
