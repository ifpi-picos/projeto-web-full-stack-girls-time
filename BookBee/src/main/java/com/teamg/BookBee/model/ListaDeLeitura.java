package com.teamg.BookBee.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "lista_de_leitura")
public class ListaDeLeitura {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lista")
    private Long idLista;

    @Column(name = "nome")
    private String nomeLista;

    @ManyToOne
    @JoinColumn(name = "id_leitor")
    private Leitor leitor;

    @ManyToMany
    @JoinTable(
        name = "lista_livros",
        joinColumns = @JoinColumn(name = "id_lista"),
        inverseJoinColumns = @JoinColumn(name = "id_livro"))
        private Set<Livro> livros;
        
    public ListaDeLeitura() {
    }
        
    public ListaDeLeitura(Leitor leitor, Livro livro, String nomeLista) {
        this.leitor = leitor;
        this.livros = new HashSet<>();
        this.livros.add(livro);
        this.nomeLista = nomeLista;
    }

    public Long getIdLista() {
        return idLista;
    }

    public Leitor getLeitor() {
        return leitor;
    }

    public String getNomeLista() {
        return this.nomeLista;
    }
    
    public Set<Livro> getLivros() {
        return this.livros;
    }
    
}
