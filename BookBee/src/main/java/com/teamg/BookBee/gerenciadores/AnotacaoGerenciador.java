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
        if(anotacaotxt.length() > 245){
            throw new IllegalArgumentException("Texto da nota não aceito por ser muito grande");
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
        List<Anotacao> anotacao = repo.findByLeitorAndLivroAndDeletadoFalse(leitor, livro);
        if(anotacao == null) {
            throw new Exception("anotacoes não encontrado para o email: " + leitor.getEmail());
        }
        LOGGER.info("Anotacoes encontrada(s) com sucesso pelo email: {}", leitor.getEmail());
        return anotacao;
    }

    public List<Anotacao> encontrarAnotacoesPorLeitor(Leitor leitor) throws Exception {
        LOGGER.info("Buscando notas do leitor pelo email: {}", leitor.getEmail());
        List<Anotacao> anotacao = repo.findByLeitorAndDeletadoFalse(leitor);
        if(anotacao == null) {
            throw new Exception("anotacoes não encontrado para o email: " + leitor.getEmail());
        }
        LOGGER.info("Anotacoes encontrada(s) com sucesso pelo email: {}", leitor.getEmail());
        return anotacao;
    }
    
    public void deletarNota(Long idAnotacao, String email) throws Exception {
        LOGGER.info("Tentando deletar a anotação com id: {}", idAnotacao);
        Anotacao anotacao = repo.findByIdAnotacaoAndDeletadoFalse(idAnotacao);
        if(anotacao == null) {
            LOGGER.error("Anotação não encontrada para o id: {}", idAnotacao);
            throw new Exception("Anotação não encontrada para o id: " + idAnotacao);
        }
        if(!anotacao.getLeitor().getEmail().equals(email)) {
            LOGGER.error("Usuário com email: {} tentou deletar uma anotação que não pertence a ele", email);
            throw new Exception("Você não tem permissão para deletar esta anotação");
        }
        anotacao.setDeletado(true);
        repo.save(anotacao);
        LOGGER.info("Anotação com id: {} foi marcada como deletada", idAnotacao);
    }
        
}
