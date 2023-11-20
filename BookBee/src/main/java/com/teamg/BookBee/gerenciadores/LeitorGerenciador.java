package com.teamg.BookBee.gerenciadores;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.teamg.BookBee.model.Leitor;
import com.teamg.BookBee.model.Livro;
import com.teamg.BookBee.repositorios.LeitorRepositorio;

@Service
public class LeitorGerenciador {

    private static final Logger LOGGER = LoggerFactory.getLogger(LeitorGerenciador.class);
    
    @Autowired
    private LeitorRepositorio repo;

    @Autowired
    private LivroGerenciador livroGerenciador;

    public void cadastra(Leitor leitor){
        LOGGER.info("Cadastrando Leitor com o email: {}", leitor.getEmail());
        String nome = leitor.getNome();
        String email = leitor.getEmail();
        String senha = leitor.getPassword();
        int pos = email.indexOf("@");
        String checarEmail = email.substring(pos, email.length() -1);
        if(nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não podem ser vazio");
        }
        if(!checarEmail.equals("@gmail.com")){
            throw new IllegalArgumentException("enderço de email é invalido");
        }
        if(senha.trim().isEmpty()){
            throw new IllegalArgumentException("Senha não pode estar vazia");
        }
        if(senha.length() < 7){
            throw new IllegalArgumentException("Senha não pode ser curta");
        }
        Leitor leitorExiste = repo.findByEmail(leitor.getUsername());
        if(leitorExiste != null) {
            throw new IllegalArgumentException("Email já existe");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(leitor.getPassword());
        leitor.setSenha(encryptedPassword);
        this.repo.save(leitor);
        LOGGER.info("Leitor cadastrado com sucesso com o email: {}", leitor.getUsername());
    } 

    public Leitor findLeitorByEmail(String email) throws Exception{
        LOGGER.info("Buscando leitor pello email: {}", email); 
        Leitor leitor = this.repo.findByEmail(email);
        if(leitor == null) {
            throw new IllegalArgumentException("Usuario não encontrado" + email);
        }
        LOGGER.info("Leitor encontrado com sucesso pelo email: {}", email);
        return leitor;
    }

    public double calcularVelocidade(Long idLivro, String email) throws Exception {
        LOGGER.info("Calculando velocidade do leitor com o email: {}", email);
        Leitor leitor = this.repo.findByEmail(email);
         if(leitor == null) {
            throw new Exception("Usuario não encontrado" + email);
        }

        Optional<Livro> livroOptiona = livroGerenciador.getLivro(idLivro);
        if(!livroOptiona.isPresent()){
            return 0.0;
        }
        Livro livro = livroOptiona.get();
        if(livro.getDataDeIni() == null){
            return 0.0;
        }

        LocalDate dataDeInicio = livro.getDataDeIni();
        LocalDate dataFinal = LocalDate.now();
        if (livro.getDataDeFim() != null) {
            dataFinal = livro.getDataDeFim();
        }
        long diferencaDias = ChronoUnit.DAYS.between(dataDeInicio, dataFinal);

        DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));

        double velocidadeLeitura;
        if (diferencaDias == 0) {
            velocidadeLeitura = livro.getPgLidas();
        } else {
            velocidadeLeitura = (double) livro.getPgLidas() / diferencaDias;
        }
        if (!Double.isFinite(velocidadeLeitura)) {
            LOGGER.error("Velocidade de leitura calculada é Infinity ou NaN para o usuário com o email: {}", email);
            return 0.0;
        }        
        
        velocidadeLeitura = Double.valueOf(df.format(velocidadeLeitura));
        LOGGER.info("Velocidade de leitura calculada com sucesso para o usuario com o email: {}", email);
        return velocidadeLeitura;
    }

}
