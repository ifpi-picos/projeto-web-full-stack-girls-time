package com.teamg.BookBee.gerenciadores;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamg.BookBee.model.Anotacao;
import com.teamg.BookBee.model.Leitor;
import com.teamg.BookBee.model.Livro;
import com.teamg.BookBee.repositorios.AnotacoesRepositorio;

@Service
public class AnotacaoGerenciador {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnotacaoGerenciador.class);

    @Autowired
    private AnotacoesRepositorio repo;

    @Autowired  
    private LeitorGerenciador leitorGerenciador;

    @Autowired
    private LivroGerenciador livroGerenciador;

    public  Map<String, Object> findNotasByEmail(String email) throws Exception{ 
        LOGGER.info("Buscando notas pelo email: {}", email);
        Map<String, Object> model = new HashMap<>();
        Leitor leitor = leitorGerenciador.findLeitorByEmail(email);
        if(leitor == null) {
            throw new Exception("Usuario Nao encontrado" + email);
        }
        List<Anotacao> anotacoes = repo.findByLeitor(leitor);
        model.put("anotacoes", anotacoes);
        LOGGER.info("Notas encontradas com sucesso pelo email: {}", email);
        return model;
    }

    public void criarNota(String anotacaotxt, Long idLivros, String email) throws Exception {
        LOGGER.info("Criando nota para o livro com ID: {} e email: {}", idLivros, email);
        if ( anotacaotxt == null || anotacaotxt.isEmpty()) {
            throw new IllegalArgumentException("Os parâmetros não podem ser nulos ou vazios");
        }
    
        
        Leitor leitor = leitorGerenciador.findLeitorByEmail(email);
        Optional<Livro> livroOptional = livroGerenciador.getLivro(idLivros);

        if(!livroOptional.isPresent()){
            throw new IllegalArgumentException("As notacoes devem ter um livro");
        }
        Livro livro = livroOptional.get();
        
        Anotacao anotacao = new Anotacao(leitor, livro, anotacaotxt);
    
        LOGGER.info("Nota criada com sucesso para o livro com ID: {} e email:", idLivros, email);
        repo.save(anotacao);
    }

    public List<Anotacao> encontrarAnotacoesPorLeitorELivro(Leitor leitor, Livro livro) throws Exception {
        LOGGER.info("Buscando notas do leitor pelo email: {} e livro id: {}", leitor.getEmail(), livro.getIdLivro());
        List<Anotacao> anotacao = repo.findByLeitorAndLivro(leitor, livro);
        if(anotacao == null) {
            throw new Exception("anotacoes não encontrado para o email: " + leitor.getEmail());
        }
        LOGGER.info("Anotacoes encontrada(s) com sucesso pelo email: {}", leitor.getEmail());
        return anotacao;
    }

    public List<Anotacao> encontrarAnotacoesPorLeitor(Leitor leitor) throws Exception {
        LOGGER.info("Buscando notas do leitor pelo email: {}", leitor.getEmail());
        List<Anotacao> anotacao = repo.findByLeitor(leitor);
        if(anotacao == null) {
            throw new Exception("anotacoes não encontrado para o email: " + leitor.getEmail());
        }
        LOGGER.info("Anotacoes encontrada(s) com sucesso pelo email: {}", leitor.getEmail());
        return anotacao;
    }
        
}
