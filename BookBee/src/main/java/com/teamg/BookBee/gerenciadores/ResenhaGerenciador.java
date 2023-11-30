package com.teamg.BookBee.gerenciadores;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamg.BookBee.model.Leitor;
import com.teamg.BookBee.model.Livro;
import com.teamg.BookBee.model.Resenha;
import com.teamg.BookBee.repositorios.ResenhaRepositorio;

@Service
public class ResenhaGerenciador {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResenhaGerenciador.class);

    @Autowired
    private ResenhaRepositorio repo;

    @Autowired
    private LeitorGerenciador leitorGerenciador;

    @Autowired
    private LivroGerenciador livroGerenciador;

   public void criarOuAtualizarResenha(String resenhaTxt, Long idLivro, String email) throws Exception {
        LOGGER.info("Criando ou atualizando resenha para livro com ID: {} e emmail: {}", idLivro, email);
        if (resenhaTxt == null || resenhaTxt.trim().isEmpty()) {
            throw new IllegalArgumentException("Os parâmetros não podem ser nulos ou vazios");
        }

        Leitor leitor = leitorGerenciador.findLeitorByEmail(email);
        Optional<Livro> livroOptional = livroGerenciador.getLivro(idLivro);

        if(!livroOptional.isPresent()){
            throw new IllegalArgumentException("A resenha deve pertencer a um livro");
        }
        Livro livro = livroOptional.get();
        Optional<Resenha> resenhaOptional = repo.findByLeitorAndLivro(leitor, livro);
        if(resenhaOptional.isPresent()){
            Resenha resenhaExitrnte = resenhaOptional.get();
            resenhaExitrnte.setTextoResenha(resenhaTxt);
            repo.save(resenhaExitrnte);
            LOGGER.info("Resenha atualizada com sucesso para o livro com Id: {} e email: {}", idLivro, email);
        }else{
            Resenha resenha = new Resenha(leitor, livro, resenhaTxt);
            repo.save(resenha);
            LOGGER.info("Resenha criada com sucesso para o livro com Id: {} e email: {}", idLivro, email);
        }
    }

    public Optional<Resenha> encontrarResenhaPorLeitorELivro(Leitor leitor, Livro livro){
        LOGGER.info("Buscando resenha do leitor pelo email: {} e do livro id: {}", leitor.getEmail(), livro.getIdLivro());
        
            Optional<Resenha> resenhas = repo.findByLeitorAndLivroAndDeletadoFalse(leitor, livro);
            if(!resenhas.isPresent()) {
                LOGGER.info("Nem uma resenha encontrada pelo email: {} e livro id: {}", leitor.getEmail(), livro.getIdLivro());
                return Optional.empty();
            }
            LOGGER.info("Resenha encontrada com sucesso pelo email: {} e livro id: {}", leitor.getEmail(), livro.getIdLivro());
            return resenhas;
    }

    public void deletarResenha(Long idResenha, String email)throws Exception {
        Resenha resenha = repo.findByIdResenhaAndDeletadoFalse(idResenha);
        if(resenha == null) {
            throw new IllegalArgumentException("Resenha não encontrada para o id: " + idResenha);
        }
        if(!resenha.getLeitor().getEmail().equals(email)) {
            throw new IllegalArgumentException("Você não tem permissão para deletar esta resenha");
        }
        resenha.setDeletado(true);
        repo.save(resenha);
    }

}
