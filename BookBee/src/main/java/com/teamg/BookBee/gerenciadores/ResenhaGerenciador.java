package com.teamg.BookBee.gerenciadores;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamg.BookBee.model.Leitor;
import com.teamg.BookBee.model.Livro;
import com.teamg.BookBee.model.Resenha;
import com.teamg.BookBee.repositorios.ResenhaRepositorio;

@Service
public class ResenhaGerenciador {
    @Autowired
    private ResenhaRepositorio repo;

    @Autowired
    private LeitorGerenciador leitorGerenciador;

    @Autowired
    private LivroGerenciador livroGerenciador;

   public void criarResenha(Resenha resenhaTxt, Long idLivro, String email) throws Exception {
    if (resenhaTxt == null || resenhaTxt.getTextoResenha().isEmpty() || resenhaTxt.getTituloResenha().isEmpty()) {
        throw new IllegalArgumentException("Os parâmetros não podem ser nulos ou vazios");
    }

    Leitor leitor = leitorGerenciador.findLeitorByEmail(email);
    Optional<Livro> livroOptional = livroGerenciador.getLivro(idLivro);

    if(!livroOptional.isPresent()){
        throw new IllegalArgumentException("A resenha deve pertencer a um livro");
    }
    Livro livro = livroOptional.get();

    Resenha resenha = new Resenha(leitor, livro, resenhaTxt.getTituloResenha(), resenhaTxt.getTextoResenha());

    repo.save(resenha);
}


}
