package com.teamg.BookBee.gerenciadores;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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

    @Autowired 
    private ListaDeLeituraGerenciador listaDeLeituraGerenciador;

    public void cadastra(Leitor leitor){
        LOGGER.info("Cadastrando Leitor com o email: {}", leitor.getEmail());
        String nome = leitor.getNome();
        String email = leitor.getEmail();
        String senha = leitor.getPassword();
        int pos = email.indexOf("@");
        String checarEmail = email.substring(pos, email.length());
        if(nome.trim().isEmpty()) {
            LOGGER.warn("Nem um nome foi inserido no campo");
            throw new IllegalArgumentException("Nome não podem ser vazio");
        }
        if(!checarEmail.equals("@gmail.com")){
            LOGGER.warn("O endereco de email nao e valido");
            throw new IllegalArgumentException("enderço de email é invalido");
        }
        if(senha.trim().isEmpty()){
            LOGGER.warn("Nem um valor foi inserido na senha");
            throw new IllegalArgumentException("Senha não pode estar vazia");
        }
        if(senha.length() < 7){
            LOGGER.warn("A senha nao corresponde ao numero de caracteres minimo");
            throw new IllegalArgumentException("Senha não pode ser curta");
        }
        Leitor leitorExiste = repo.findByEmail(leitor.getUsername());
        if(leitorExiste != null) {
            LOGGER.warn("O endereco de email ja existe no banco de dados");
            throw new IllegalArgumentException("Email já existe");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(leitor.getPassword());
        leitor.setSenha(encryptedPassword);
        this.repo.save(leitor);
        LOGGER.info("Leitor cadastrado com sucesso com o email: {}", leitor.getUsername());
    } 

    public void atualiza(Leitor leitor, String emailAtual){
        LOGGER.info("Atualizando Leitor com o email: {}", leitor.getEmail());
        String nome = leitor.getNome();
        String email = leitor.getEmail();
        String senha = leitor.getPassword();
        int pos = email.indexOf("@");
        String checarEmail = email.substring(pos, email.length());
        if(nome != null && !nome.trim().isEmpty()) {
            if(nome.trim().isEmpty()) {
                LOGGER.warn("Nem um nome foi inserido no campo");
                throw new IllegalArgumentException("Nome não podem ser vazio");
            }
        }
        if(email != null && !email.trim().isEmpty()) {
            if(!checarEmail.equals("@gmail.com")){
                LOGGER.warn("O endereco de email nao e valido");
                throw new IllegalArgumentException("enderço de email é invalido");
            }
        }
        if(senha != null && !senha.trim().isEmpty()){
            if(senha.trim().isEmpty()){
                LOGGER.warn("Nem um valor foi inserido na senha");
                throw new IllegalArgumentException("Senha não pode estar vazia");
            }
            if(senha.length() < 7){
                LOGGER.warn("A senha nao corresponde ao numero de caracteres minimo");
                throw new IllegalArgumentException("Senha não pode ser curta");
            }
        }
        Leitor leitorExiste = repo.findByEmail(emailAtual);
        if(leitorExiste != null) {
            if(nome != null && !nome.trim().isEmpty()) {
                leitorExiste.setNome(leitor.getNome());
            }
            if(email != null && !email.trim().isEmpty()) {
                leitorExiste.setEmail(leitor.getEmail());
            }
            if(senha != null && !senha.trim().isEmpty()){
                String encryptedPassword = new BCryptPasswordEncoder().encode(leitor.getPassword());
                leitorExiste.setSenha(encryptedPassword);
            }
        } else {
            LOGGER.error("O usuario: {} tentou altera dados mais o seu email anterior nao consta no banco de dados", emailAtual);
            throw new IllegalArgumentException("Email não existe");
        }
        repo.save(leitorExiste);
        LOGGER.info("Leitor atualizado com sucesso com o email: {}", leitor.getUsername());
    }
    

    public Leitor findLeitorByEmail(String email) throws Exception {
        LOGGER.info("Buscando leitor pello email: {}", email); 
        Leitor leitor = this.repo.findByEmail(email);
        if(leitor == null) {
            LOGGER.warn("O usuario nao foi encontrado: {}", email);
            throw new IllegalArgumentException("Usuario não encontrado " + email);
        }
        LOGGER.info("Leitor encontrado com sucesso pelo email: {}", email);
        return leitor;
    }
    
    public double calcularVelocidade(Long idLivro, String email) throws Exception {
        LOGGER.info("Calculando velocidade do leitor com o email: {}", email);
        Leitor leitor = this.repo.findByEmail(email);
        if(leitor == null) {
            LOGGER.error("O usuario nao foi encontrado: {}", email);
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
            LOGGER.warn("Velocidade de leitura calculada é Infinity ou NaN para o usuário com o email: {}", email);
            return 0.0;
        }        
        
        velocidadeLeitura = Double.valueOf(df.format(velocidadeLeitura));
        LOGGER.info("Velocidade de leitura calculada com sucesso para o usuario com o email: {}", email);
        return velocidadeLeitura;
    }

    public Map<String, Object>  getDadosDoUsuario(String subject) throws Exception {
        LOGGER.info("Obtendo as informacoes do usuario: {}", subject);
        Map<String, Object> model = new HashMap<>();
        Leitor leitor = repo.findByEmail(subject);
        Map<String, Object> listaDeLeitura = listaDeLeituraGerenciador.getListasDoUsuario(subject);
        model.put("totalPgLidas", getTotalPaginasLidas(subject));
        model.put("totalLivrosLidos", getTotalLivrosTerminados(subject));
        model.put("totalListas", getTotalListasDeLeitura(subject));
        model.put("leitor", leitor);
        model.putAll(listaDeLeitura);
        return model;
    }

    public int getTotalPaginasLidas(String subject) throws Exception {
        LOGGER.info("Calculando o total de paginas lidas pelo leitor: {}", subject);
        Leitor leitor = findLeitorByEmail(subject);
        Set<Integer> paginasLidas = new HashSet<>();
        for (Livro livro : leitor.getLivros()) {
            paginasLidas.add(livro.getPgLidas());
        }
        int total = 0;
        for (int paginas : paginasLidas) {
            total += paginas;
        }
        LOGGER.info("Total de paginas calculado com sucesso");
        return total;
    }

    public int getTotalLivrosTerminados(String subject) throws Exception {
        LOGGER.info("Calculando o total de livros lido para o leito: {}", subject);
        Leitor leitor = findLeitorByEmail(subject);
        int total = 0;
        for (Livro livro : leitor.getLivros()) {
            if (livro.getPgLidas() == livro.getPaginas()) {
                total++;
            }
        }
        LOGGER.info("Total de livros calculado com sucesso");
        return total;
    }

    public int getTotalListasDeLeitura(String subject) throws Exception {
        LOGGER.info("Calculando o total de lista do usuario");
        Leitor leitor = findLeitorByEmail(subject);
        return leitor.getListaDeLeituras().size();
    }
    

}
