package com.teamg.BookBee.repositorios;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.teamg.BookBee.model.Anotacao;
import com.teamg.BookBee.model.Leitor;
import com.teamg.BookBee.model.Livro;

public interface AnotacoesRepositorio extends CrudRepository<Anotacao, Long>{

    List<Anotacao> findByLeitor(Leitor leitor);

    List<Anotacao> findByLeitorAndLivro(Leitor leitor, Livro livro);

}
