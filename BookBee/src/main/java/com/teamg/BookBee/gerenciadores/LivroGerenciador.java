package com.teamg.BookBee.gerenciadores;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import com.teamg.BookBee.model.Resenha;
import com.teamg.BookBee.repositorios.LivroRepositorio;

@Service
public class LivroGerenciador {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(LivroGerenciador.class);

    @Autowired
    private LivroRepositorio livroRepo;

    @Autowired
    private AnotacaoGerenciador anotacaoGerenciador;

    @Autowired
    private LeitorGerenciador leitorGerenciador;

    @Autowired
    private ResenhaGerenciador resenhaGerenciador;

    public Map<String, Object> getLivrosEAnotacoes(String subject) throws Exception{
        LOGGER.info("Iniciando getLivroEAnotacoes para pegar os livro e anotacoes do usuario: {}", subject);
        Map<String, Object> model = new HashMap<>();
        Leitor leitor = leitorGerenciador.findLeitorByEmail(subject);
        
        if(leitor != null) {
            List<Livro> livros = (List<Livro>)livroRepo.findByLeitor(leitor);
            List<Livro> livrosRecentes = (List<Livro>)livroRepo.findByLeitorOrderByDataDeAtualizacaoDesc(leitor);
            List<Anotacao> listaDeAnotacoes = (List<Anotacao>)anotacaoGerenciador.encontrarAnotacoesPorLeitor(leitor);
            List<Livro> livrosFavoritoList = (List<Livro>)livroRepo.findByLeitorAndFavorito(leitor, true);
                
            model.put("livrosF", livrosFavoritoList);
            model.put("livros", livros);
            model.put("livrosR", livrosRecentes);
            model.put("anotacoes", listaDeAnotacoes);
            LOGGER.info("Finalizando o getLivrosEAnotacoes para o email: {}", subject);
        }
        return model;
    }

    public Map<String, Object> getNotasEResenhaDoLivro(Long id, String subject) throws Exception {
        LOGGER.info("Inicio da busca de anotações do livro {} pelo usuário {}", id, subject);
        Map<String, Object> model = new HashMap<>();
        Optional<Livro> optionalLivro = livroRepo.findById(id);

        if(optionalLivro.isPresent()) {
            Livro livro = optionalLivro.get();
            Leitor leitorlivro = livro.getLeitor();
            Leitor leitorParam = this.leitorGerenciador.findLeitorByEmail(subject);

            if(leitorParam != null && leitorlivro.getId() == leitorParam.getId()) {
                List<Anotacao> anotacoes = 
                (List<Anotacao>)anotacaoGerenciador.encontrarAnotacoesPorLeitorELivro(leitorParam, livro);
                Optional<Resenha> resenha = resenhaGerenciador.encontrarResenhaPorLeitorELivro(leitorParam, livro);
                
                model.put("livro", livro);
                model.put("anotacoes", anotacoes);
                model.put("resenha", resenha);
                LOGGER.info("Fim da busca de anotações do livro {} pelo usuário {}", id, subject);
            }
        }
        return model;
    }

    public Optional<Livro> getLivro(Long id){
        try{
            LOGGER.info("Buscando livro com ID: {}", id);
            Optional<Livro> optionalLivro = livroRepo.findById(id);
            if(!optionalLivro.isEmpty()){
                return optionalLivro;
            }
        } catch (IllegalArgumentException e ) {
            LOGGER.error("ID invalido fornecido: {} erro: {} ", id, e.getMessage());
        } catch ( Exception e) {
            LOGGER.error("Erro ao buscar livro: {} erro: {}", id,  e.toString());
        }
        return null;
    }

    public void adicionar(Livro livro, String subject) throws Exception {
        LOGGER.info("Tentando adicionar livro para o usuario com email: {}", subject);
        Leitor leitor = leitorGerenciador.findLeitorByEmail(subject);
        if(leitor == null){
            LOGGER.error("Leitor nao encontrado email: {}", subject);
            throw new IllegalArgumentException("Leitor nao encontrado");
        }
        
        livro.setLeitor(leitor);
        livro.setDataDeAtualizacao(LocalDateTime.now());
        livroRepo.save(livro);
        LOGGER.info("Livro adicionado com sucesso para o email: {}", subject);
        return;
    }

    public void verificarLivroELeitor(Livro livro, LocalDate data, String subject) throws Exception{
        LOGGER.info("Verificando se o livro existe no banco de dados e pertence a: {}", subject);
        if( livro == null || data == null){
            LOGGER.error("Dados invalidos ou nulos");
            throw new IllegalArgumentException("Livro ou data nao podem ser nulos");
        }
        Leitor leitor = leitorGerenciador.findLeitorByEmail(subject);
        Leitor leitorLivro = livro.getLeitor();
        if(leitor == null || leitorLivro == null){
            LOGGER.error("O leitor nao foi encontrado");
            throw new IllegalArgumentException("Nao foi possivel encontrar um leitor com o ID fornecido");
        }
        if(leitor.getId() != leitorLivro.getId()){
            LOGGER.error("O Livro nao pertence a este leitor");
            throw new IllegalArgumentException("O livro nao pertecea ao usuario");
        }
    }

    public void atualizarDataIni(Livro livro, LocalDate dataInicio,  String subject) throws Exception{
        LOGGER.info("Atualizando data de inicio para o livro: {} do usuario: {}", livro.getTitulo(), subject);
        verificarLivroELeitor(livro, dataInicio, subject);
        livro.setDataDeIni(dataInicio);
        livro.setDataDeAtualizacao(LocalDateTime.now());
        livroRepo.atualizarDataDeInicio(livro.getIdLivro(), dataInicio);
        LOGGER.info("Data de início atualizada com sucesso para o livro do: {}", subject);
    }

    public void atualizarDataFim(Livro livro, LocalDate dataFim,  String subject) throws Exception{
        LOGGER.info("Atualizando data de Fim para o livro: {} do usuario: {}", livro.getTitulo(), subject);
        verificarLivroELeitor(livro, dataFim, subject);
        livro.setDataDeFim(dataFim);
        livro.setPgLidas(livro.getPaginas());
        livro.setDataDeAtualizacao(LocalDateTime.now());
        livroRepo.atualizarDataDeFim(livro.getIdLivro(), dataFim);
        livroRepo.atualizaPgLidas(livro.getIdLivro(), livro.getPgLidas());
        LOGGER.info("Data de Fim atualizada com sucesso para o livro do: {}", subject);
    }

    public void atualizarPossicaoDeLeitura(Livro livroParam, int novasPaginasLidas) throws Exception{
        LOGGER.info("Atualizando posicoes de leitura para o livro com ID: {}", livroParam.getIdLivro());
        Optional<Livro> livroOptional = getLivro(livroParam.getIdLivro());
        if(!livroOptional.isPresent()){
            LOGGER.error("Não foi possível localizar o livro com id: {}", livroParam.getIdLivro());
            throw new Exception("Livro nao encontrado");
        }
        Livro livro = livroOptional.get();
        if (novasPaginasLidas <= livro.getPgLidas()) {
            LOGGER.warn("A quantidade de paginas lida é menor que a anterior");
            throw new Exception("O novo valor das páginas lidas deve ser maior que o valor anterior.");
        }
        if (novasPaginasLidas > livro.getPaginas()) {
            LOGGER.warn("A quantidade de paginas lida é superior a quantidade total de paginas do livro");
            throw new Exception("O novo valor das páginas lidas não pode ser maior que o número total de páginas do livro.");
        }
        if (livro.getDataDeIni() == null) {
            LOGGER.info("Adicionando um data inicial apos o cadastro de paginas");
            livro.setDataDeIni(LocalDate.now());
            livroRepo.atualizarDataDeInicio(livro.getIdLivro(), livro.getDataDeFim());
            
        }
        if (novasPaginasLidas == livro.getPaginas()) {
            LOGGER.info("Adicionando uma data fim apos o cadstro de paginas ser igual ao numero de Paginas do Livro");
            livro.setDataDeFim(LocalDate.now());
            livroRepo.atualizarDataDeFim(livro.getIdLivro(), livro.getDataDeFim());
        }

        livro.setPgLidas(novasPaginasLidas);
        livroRepo.atualizaPgLidas(livro.getIdLivro(), novasPaginasLidas);
        LOGGER.info("Posicao de leitura atualizada com sucesso para o livro com ID:", livro.getIdLivro());
    
    }

    public Map<String, Object> getTodosOsLivros(String subject) throws Exception {
        LOGGER.info("Obtendo todos os livro para o usuario: {}", subject);
        Map<String, Object> model = new HashMap<>();
        Leitor leitor = leitorGerenciador.findLeitorByEmail(subject);
        List<Livro> livros = (List<Livro>)livroRepo.findByLeitor(leitor);
        if (livros.isEmpty()) {
            LOGGER.info("Nem um livro cadastrado para o usuário: {}", subject);
            throw new Exception("Nem um livro encontrado");
        }
        model.put("livros", livros);
        LOGGER.info("Todos os livros obtidos com sucesso para o assunto: {}", subject);
        return model;
    }

    public void atualizarClassificacao(Long id, String classificacao) {
        LOGGER.info("Adicionando a classificacao do livro: {}", id);
        int classificacaoInt = Integer.parseInt(classificacao);
  
        Optional<Livro> livroExistente = livroRepo.findById(id);
        if (!livroExistente.isPresent()) {
            LOGGER.error("Erro ao tentar adicionar a clasificação do livro: {}, pois ele não existe.", id);
            throw new IllegalArgumentException("Livro não encontrado");
        }
        Livro livro = livroExistente.get();
        if (classificacaoInt < 1 || classificacaoInt > 5) {
            LOGGER.error("Clasificacao inválida. Clasificacao aceita entre 1 e 5 estrelas");
            throw new IllegalArgumentException("Classificação inválida");
        }
        livro.setDataDeAtualizacao(LocalDateTime.now());
        livro.setClassificacao(classificacaoInt);
        livroRepo.save(livro);
        LOGGER.info("A clasificação foi atualizado com sucesso para o livro: {}", id);
    }

    public void atualizarFavorito(Long id, String favoritoString) {
        LOGGER.info("Adicionando o livro: {} ao favoritos.", id );
        boolean favorito = Boolean.parseBoolean(favoritoString);

        if (id == null) {
            LOGGER.error("ID do livro é obrigatório.");
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        Optional<Livro> livroExistente = livroRepo.findById(id);
        if (!livroExistente.isPresent()) {
            LOGGER.error("Erro ao tentar adicionar o livro: {}, pois ele não existe.", id);
            throw new IllegalArgumentException("Livro não encontrado");
        }
        
        Livro livro = livroExistente.get();
    
        livro.setDataDeAtualizacao(LocalDateTime.now());
        livro.setFavorito(favorito);
        livroRepo.save(livro);
        LOGGER.info("O livro foi atualizado com sucesso como favorito ou nao para o ID: {}", id);
       
    }

    public Map<String, Object> getLivrosFavoritos(String subject) throws Exception {
        LOGGER.info("Obtendo todos os livros favoritos para o usuario: {}", subject);
        Map<String, Object> model = new HashMap<>();
        Leitor leitor = leitorGerenciador.findLeitorByEmail(subject);
        List<Livro> livros = (List<Livro>)livroRepo.findByLeitorAndFavorito(leitor, true);
        if (livros.isEmpty()) {
            LOGGER.warn("Nem um livro favorito foi encontrado");
            throw new Exception("Nem um livro favorito encontrado");
        }
        model.put("livros", livros);
        LOGGER.info("Todos os livros favoritos obtidos com sucesso para o assunto: {}", subject);
        return model;
    }

    public Map<String, Object> getLivrosEmProgresso(String subject) throws Exception {
        LOGGER.info("Obtendo todos os livros em progresso para o usuario: {}", subject);
        Map<String, Object> model = new HashMap<>();
        Leitor leitor = leitorGerenciador.findLeitorByEmail(subject);
        List<Livro> livros = (List<Livro>)livroRepo.findByLeitorAndPgLidasNotEqualPaginas(leitor);
        if (livros.isEmpty()) {
            LOGGER.warn("Nem um livro em andamento foi encontrado");
            throw new Exception("Nem um livro encontrado");
        }
        model.put("livros", livros);
        LOGGER.info("Todos os livros em progresso obtidos com sucesso para o usuario: {}", subject);
        return model;
    }

}
