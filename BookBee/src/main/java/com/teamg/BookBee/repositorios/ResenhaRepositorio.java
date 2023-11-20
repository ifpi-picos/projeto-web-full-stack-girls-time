package com.teamg.BookBee.repositorios;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.teamg.BookBee.model.Leitor;
import com.teamg.BookBee.model.Livro;
import com.teamg.BookBee.model.Resenha;

public interface ResenhaRepositorio extends CrudRepository<Resenha, Long> {

    Optional<Resenha> findByLeitorAndLivro(Leitor leitor, Livro livro);

    Resenha findByIdResenhaAndDeletadoFalse(Long idResenha);

    Optional<Resenha> findByLeitorAndLivroAndDeletadoFalse(Leitor leitor, Livro livro);


}
