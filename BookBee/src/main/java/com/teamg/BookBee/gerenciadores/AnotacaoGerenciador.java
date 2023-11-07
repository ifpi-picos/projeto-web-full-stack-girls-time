package com.teamg.BookBee.gerenciadores;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.teamg.BookBee.model.Anotacao;
import com.teamg.BookBee.model.Leitor;
import com.teamg.BookBee.model.Livro;
import com.teamg.BookBee.repositorios.AnotacoesRepositorio;

public class AnotacaoGerenciador {
    @Autowired
    private AnotacoesRepositorio repo;

    @Autowired
    private LeitorGerenciador leitorGerenciador;

    @Autowired
    private LivroGerenciador livroGerenciador;

    public  Map<String, Object> findNotasByEmail(String email) throws Exception{ 
        Map<String, Object> model = new HashMap<>();
        Leitor leitor = leitorGerenciador.findLeitorByEmail(email);
        if(leitor == null) {
            throw new Exception("Usuario Nao encontrado" + email);
        }
        List<Anotacao> anotacoes = repo.findByLeitor(leitor);
        model.put("anotacoes", anotacoes);
        return model;
    }

    public void criarNota(Anotacao anotacaotxt, Long idLivros, String email) throws Exception {
            if ( anotacaotxt == null || anotacaotxt.getAnotacao().isEmpty()) {
                throw new IllegalArgumentException("Os parâmetros não podem ser nulos ou vazios");
            }
        
          
            Leitor leitor = leitorGerenciador.findLeitorByEmail(email);
            Optional<Livro> livroOptional = livroGerenciador.getLivro(idLivros);

            if(!livroOptional.isPresent()){
                throw new IllegalArgumentException("As notacoes devem ter um livro");
            }
            Livro livro = livroOptional.get();
         
            Anotacao anotacao = new Anotacao(leitor, livro, anotacaotxt.getAnotacao(), anotacaotxt.getCapitulo(), anotacaotxt.getPagina());
        
          
            repo.save(anotacao);
        }
        
}
