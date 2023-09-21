package com.teamg.BookBee.repositorios;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.teamg.BookBee.model.Leitor;
import com.teamg.BookBee.model.Livro;

public interface LivroRepositorio extends CrudRepository<Livro, Long>{

    List<Livro> findByLeitor(Leitor leitor);

    List<Livro> findByLeitorOrderByDataDeAtualizacaoDesc(Leitor leitor);
    
}


