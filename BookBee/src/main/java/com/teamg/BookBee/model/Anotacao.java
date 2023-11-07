package com.teamg.BookBee.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name= "anotacoes")
public class Anotacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_anotacao")
    private Long idAnotacao;

    @ManyToOne
    @JoinColumn(name = "leitor_id")
    private Leitor leitor;

    @ManyToOne
    @JoinColumn(name = "livro_id")
    private Livro livro;

    @Column(name = "anotacao")
    private String anotacao;

    @Column(name = "capitulo")
    private String capitulo;

    @Column(name = "pagina")
    private int pagina;

    public Anotacao(Leitor leitor, Livro livro, String anotacao, String capitulo, int pagina) {
        this.leitor = leitor;
        this.livro = livro;
        this.anotacao = anotacao;
        this.capitulo = capitulo;
        this.pagina = pagina;
    }

    public Long getIdAnotacao() {
        return idAnotacao;
    }

    public Leitor getLeitor() {
        return leitor;
    }

    public Livro getLivro() {
        return livro;
    }

    public String getAnotacao() {
        return anotacao;
    }

    public String getCapitulo() {
        return capitulo;
    }

    public int getPagina() {
        return pagina;
    }

}
