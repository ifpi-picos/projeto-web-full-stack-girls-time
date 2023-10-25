package com.teamg.BookBee.gerenciadores;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.teamg.BookBee.model.Anotacao;
import com.teamg.BookBee.model.Leitor;
import com.teamg.BookBee.repositorios.AnotacoesRepositorio;

public class AnotacaoGerenciador {
    @Autowired
    private AnotacoesRepositorio repo;

    @Autowired
    private LeitorGerenciador leitorGerenciador;

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
}
