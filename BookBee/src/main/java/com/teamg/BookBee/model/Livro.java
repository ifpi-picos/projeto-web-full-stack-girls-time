package com.teamg.BookBee.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_livro")
    private Long idLivro;

    @ManyToOne
    @JoinColumn(name = "id_leitor")
    private Leitor leitor;

    @OneToMany(mappedBy = "livro")
    private Set<Anotacao> anotacoes;

    @OneToOne(mappedBy = "livro")
    private Resenha resenha;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "link_imagens")
    private String linkImagem;

    @Column(name = "autor")
    private String autor;

    @Column(name = "genero")
    private String genero;

    @Column(name = "paginas")
    private int paginas;

    @Column(name = "pg_lidas")
    private int pgLidas;

    @Column(name = "favorito")
    private boolean favorito;

    @Column(name= "classificacao")
    private int classificacao;

    @Column(name = "data_de_ini")
    private LocalDate dataDeIni;

    @Column(name = "data_de_fim")
    private LocalDate dataDeFim;

    @Column(name = "data_de_atualizacao")
    private LocalDateTime dataDeAtualizacao;

    public Long getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(Long idLivro) {
        this.idLivro = idLivro;
    }

    public Leitor getLeitor() {
        return leitor;
    }

    public void setLeitor(Leitor leitor) {
        this.leitor = leitor;
    }

    public Set<Anotacao> getAnotacoes() {
        return anotacoes;
    }

    public void setAnotacoes(Set<Anotacao> anotacoes) {
        this.anotacoes = anotacoes;
    }

    public Resenha getResenha() {
        return resenha;
    }

    public void setResenha(Resenha resenha) {
        this.resenha = resenha;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getLinkImagem() {
        return linkImagem;
    }

    public void setLinkImagem(String linkImagem) {
        this.linkImagem = linkImagem;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getPaginas() {
        return paginas;
    }

    public void setPaginas(int paginas) {
        this.paginas = paginas;
    }

    public int getPgLidas() {
        return pgLidas;
    }

    public void setPgLidas(int pgLidas) {
        this.pgLidas = pgLidas;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    public int getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(int classificacao) {
        this.classificacao = classificacao;
    }

    public LocalDate getDataDeIni() {
        return dataDeIni;
    }

    public void setDataDeIni(LocalDate dataDeIni) {
        this.dataDeIni = dataDeIni;
    }

    public LocalDate getDataDeFim() {
        return dataDeFim;
    }

    public void setDataDeFim(LocalDate dataDeFim) {
        this.dataDeFim = dataDeFim;
    }

    public LocalDateTime getDataDeAtualizacao() {
        return dataDeAtualizacao;
    }

    public void setDataDeAtualizacao(LocalDateTime dataDeAtualizacao) {
        this.dataDeAtualizacao = dataDeAtualizacao;
    }

    
}
