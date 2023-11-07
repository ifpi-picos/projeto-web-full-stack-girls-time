package com.teamg.BookBee.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "resenhas")
public class Resenha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resenha")
    private Long idResenha;

    @ManyToOne
    @JoinColumn(name = "id_leitor",
    referencedColumnName="id_leitor")
    private Leitor leitor;
    
    @OneToOne
    @JoinColumn(name = "id_livro", referencedColumnName="id_livro")
    private Livro livro;

    @Column(name = "titulo_resenha")
    private String tituloResenha;
    
    @Lob
    @Column(name = "texto_resenha")
    private String textoResenha;

    public Resenha(Leitor leitor, Livro livro,String tituloResenha, String textoResenha) {
        this.leitor = leitor;
        this.livro = livro;
        this.tituloResenha = textoResenha;
        this.textoResenha = textoResenha;
    }

    public void setTitutloResenha(String tituloResenha) {
        this.tituloResenha = tituloResenha;
    }

    public void setTextoResenha(String textoResenha) {
        this.textoResenha = textoResenha;
    }

    public Long getIdResenha() {
        return idResenha;
    }

    public Leitor getLeitor() {
        return leitor;
    }

    public Livro getLivro() {
        return livro;
    }

    public String getTituloResenha() {
        return tituloResenha;
    }

    public String getTextoResenha() {
        return textoResenha;
    }

    
    
}
