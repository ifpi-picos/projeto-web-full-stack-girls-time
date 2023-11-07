package com.teamg.BookBee.repositorios;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.teamg.BookBee.model.Leitor;
import com.teamg.BookBee.model.Livro;

public interface LivroRepositorio extends CrudRepository<Livro, Long>{

    List<Livro> findByLeitor(Leitor leitor);

    List<Livro> findByLeitorOrderByDataDeAtualizacaoDesc(Leitor leitor);

    @Transactional
    @Modifying
    @Query("update Livro l set l.dataDeIni = :dataDeIni where l.id = :id")
    void atualizarDataDeInicio(@Param("id") Long id, @Param("dataDeIni") LocalDate dataDeIni);
    
    @Transactional
    @Modifying
    @Query("update Livro l set l.dataDeFim = :dataDeFim where l.id = :id")
    void atualizarDataDeFim(@Param("id")Long id,@Param("dataDeFim") LocalDate dataFim);

    @Transactional
    @Modifying
    @Query("update Livro l set l.pgLidas = :novasPaginasLidas where l.id = :idLivro")
    void atualizaPgLidas(@Param("idLivro") Long idLivro, @Param("novasPaginasLidas") int novasPaginasLidas);

    
}


