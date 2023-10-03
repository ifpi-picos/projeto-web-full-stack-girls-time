package com.teamg.BookBee.model;

import java.util.Set;

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
public class Leitor {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name= "id_leitor")
    private long id;

    @Column(name= "nome")
    private String nome;

    @Column(name = "nome_usuario")
    private String nomeUsuario;

    @Column(name = "email")
    @Email
    private String email;

    @Column(name= "senha")
    private String senha;

    @Column(name= "total_pg_lidas")
    private int totalPgLidas;

    @Column(name= "total_livros_lidos")
    private int totalLivrosLidos;

    @OneToMany(mappedBy = "leitor", cascade = CascadeType.ALL)
    private Set<Livro> livros;

    @OneToMany(mappedBy = "leitor")
    private Set<Anotacao> anotacoes;

    public void setId(long id) {
        this.id = id;
    }

    public Set<Anotacao> getAnotacoes() {
        return anotacoes;
    }

    public void setAnotacoes(Set<Anotacao> anotacoes) {
        this.anotacoes = anotacoes;
    }

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

    public int getTotalPgLidas() {
        return totalPgLidas;
    }

    public void setTotalPgLidas(int totalPgLidas) {
        this.totalPgLidas = totalPgLidas;
    }

    public int getTotalLivrosLidos() {
        return totalLivrosLidos;
    }

    public void setTotalLivrosLidos(int totalLivrosLidos) {
        this.totalLivrosLidos = totalLivrosLidos;
    }

    public Set<Livro> getLivros() {
        return livros;
    }

    public void setLivros(Set<Livro> livros) {
        this.livros = livros;
    }
    

}