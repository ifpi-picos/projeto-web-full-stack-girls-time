package com.teamg.BookBee.gerenciadores;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.teamg.BookBee.model.Leitor;
import com.teamg.BookBee.model.Livro;
import com.teamg.BookBee.repositorios.LeitorRepositorio;

@Service
public class LeitorGerenciador {
    
    @Autowired
    private LeitorRepositorio repo;

    public void cadastra(Leitor leitor){
        String nome = leitor.getNome();
        String email = leitor.getUsername();
        String senha = leitor.getPassword();

        if(nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não podem ser vazio");
        }
        if( email.trim().isEmpty()) {
            throw new IllegalArgumentException("O email não podem ser vazio");
        }
        if(senha.trim().isEmpty()){
            throw new IllegalArgumentException("Senha não pode estar vazia");
        }
        Leitor leitorExiste = repo.findByEmail(leitor.getUsername());
        if(leitorExiste != null) {
            throw new IllegalArgumentException("Email já existe");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(leitor.getPassword());
        leitor.setSenha(encryptedPassword);
        this.repo.save(leitor);
    } 

    public Leitor findLeitorByEmail(String email) throws Exception{ 
        Leitor leitor = this.repo.findByEmail(email);
        if(leitor == null) {
            throw new Exception("Usuario não encontrado" + email);
        }
        return leitor;
    }

    public boolean livroDoUsuario(Livro livro, String email) throws Exception{
        Leitor leitor = this.repo.findByEmail(email);
        if(leitor == null) {
            throw new Exception("Usuario não encontrado" + email);
        }
        if(!leitor.getLivros().contains(livro)) {
            throw new Exception("O livro nao pertence ao leitor");
        }
        return true;
    }

    public double calcularVelocidade(Livro livro, String email) throws Exception {
        Leitor leitor = this.repo.findByEmail(email);
         if(leitor == null) {
            throw new Exception("Usuario não encontrado" + email);
        }
        LocalDate dataDeInicio = livro.getDataDeIni();
        LocalDate dataAtual = LocalDate.now();
        long diferencaDias = ChronoUnit.DAYS.between(dataDeInicio, dataAtual);

        if(diferencaDias == 0){
            return 0.0;
        }

        double velocidadeLeitura = (double) livro.getPgLidas() / diferencaDias;
        return velocidadeLeitura;
    }

}
