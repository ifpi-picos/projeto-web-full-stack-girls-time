package com.teamg.BookBee.repositorios;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.teamg.BookBee.model.Leitor;
import com.teamg.BookBee.model.Livro;

public interface LivroRepositorio extends CrudRepository<Livro, Long>{

    List<Livro> findByLeitor(Leitor leitor);

    List<Livro> findByLeitorOrderByDataDeAtualizacaoDesc(Leitor leitor);

    void atualizarDataDeInicio(@Param("id") Long id, @Param("dataDeIni") LocalDate dataDeIni);
    
}


