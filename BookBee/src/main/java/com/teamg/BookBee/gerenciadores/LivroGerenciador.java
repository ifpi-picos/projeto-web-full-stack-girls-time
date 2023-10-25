package com.teamg.BookBee.gerenciadores;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamg.BookBee.model.Anotacao;
import com.teamg.BookBee.model.Leitor;
import com.teamg.BookBee.model.Livro;
import com.teamg.BookBee.repositorios.AnotacoesRepositorio;
import com.teamg.BookBee.repositorios.LivroRepositorio;

@Service
public class LivroGerenciador {
    
    @Autowired
    private LivroRepositorio livroRepo;
    @Autowired
    private AnotacoesRepositorio anotacoesRepo;
    @Autowired
    private LeitorGerenciador leitorGerenciador;

    public Map<String, Object> getLivrosEAnotacoes(String subject) throws Exception{
        Map<String, Object> model = new HashMap<>();
        Leitor leitor = leitorGerenciador.findLeitorByEmail(subject);
        
        if(leitor != null) {
            List<Livro> livros = (List<Livro>)livroRepo.findByLeitor(leitor);
            List<Livro> livrosRecentes = (List<Livro>)livroRepo.findByLeitorOrderByDataDeAtualizacaoDesc(leitor);
            List<Anotacao> listaDeAnotacoes = (List<Anotacao>)anotacoesRepo.findByLeitor(leitor);
        

            model.put("livros", livros);
            model.put("livrosR", livrosRecentes);
            model.put("anotacoes", listaDeAnotacoes);
        }

        return model;

    }

    public Map<String, Object> getAnotacoesDoLivro(Long id, String subject) throws Exception {
        Map<String, Object> model = new HashMap<>();
        Optional<Livro> optionalLivro = livroRepo.findById(id);

        if(optionalLivro.isPresent()) {
            Livro livro = optionalLivro.get();
            Leitor leitorlivro = livro.getLeitor();
            Leitor leitorParam = this.leitorGerenciador.findLeitorByEmail(subject);
            if(leitorParam != null && leitorlivro.getId() == leitorParam.getId()) {
                List<Anotacao> anotacoes = 
                    (List<Anotacao>)anotacoesRepo.findByLeitorAndLivro(leitorlivro, livro);
                    model.put("livro", livro);
                    model.put("anotacoes", anotacoes);
            }
        }
        return model;
    }

    public Optional<Livro> getLivro(Long id){
        try{
            Optional<Livro> optionalLivro = livroRepo.findById(id);
            if(!optionalLivro.isEmpty()){
                return optionalLivro;
            }
        } catch (IllegalArgumentException e ) {
            System.out.println("ID invalido fornecido: " + e.getMessage());
        } catch ( Exception e) {
            System.out.println("Erro ao buscar livro: " + e.toString());
        }
        return null;
    }

    public void adicionar(Livro livro, String subject) throws Exception {
        Leitor leitor = leitorGerenciador.findLeitorByEmail(subject);
        if(leitor == null){
            throw new IllegalArgumentException("Leitor nao encontrado");
        }
        
        livro.setLeitor(leitor);
        livro.setDataDeAtualizacao(LocalDateTime.now());
        livroRepo.save(livro);
        return;
    }

    public void verificarLivroELeitor(Livro livro, LocalDate data, String subject) throws Exception{
        if( livro == null || data == null){
            throw new IllegalArgumentException("Livro ou data nao podem ser nulos");
        }
        Leitor leitor = leitorGerenciador.findLeitorByEmail(subject);
        Leitor leitorLivro = livro.getLeitor();
        if(leitor == null || leitorLivro == null){
            throw new IllegalArgumentException("Nao foi possivel encontrar um leitor com o ID fornecido");
        }
        if(leitor.getId() != leitorLivro.getId()){
            throw new IllegalArgumentException("O livro nao pertecea ao usuario");
        }
    }

    public void atualizarDataIni(Livro livro, LocalDate dataInicio,  String subject) throws Exception{
        verificarLivroELeitor(livro, dataInicio, subject);
        livro.setDataDeIni(dataInicio);
        livro.setDataDeAtualizacao(LocalDateTime.now());
        livroRepo.atualizarDataDeInicio(livro.getIdLivro(), dataInicio);
    }

    public void atualizarDataFim(Livro livro, LocalDate dataFim,  String subject) throws Exception{
        verificarLivroELeitor(livro, dataFim, subject);
        livro.setDataDeFim(dataFim);
        livro.setDataDeAtualizacao(LocalDateTime.now());
        livroRepo.atualizarDataDeFim(livro.getIdLivro(), dataFim);
    }

    public void atualizarPossicaoDeLeitura(Livro livro, int novasPaginasLidas) throws Exception{
        if (novasPaginasLidas <= livro.getPgLidas()) {
            throw new Exception("O novo valor das páginas lidas deve ser maior que o valor anterior.");
        }
        if (novasPaginasLidas > livro.getPaginas()) {
            throw new Exception("O novo valor das páginas lidas não pode ser maior que o número total de páginas do livro.");
        }
        livro.setPgLidas(novasPaginasLidas);
        livroRepo.atualizaPgLidas(livro.getIdLivro(), novasPaginasLidas);
    
    }

    public Map<String, Object> getTodosOsLivros(String subject) throws Exception {
        Map<String, Object> model = new HashMap<>();

        if (subject == null) {
            throw new IllegalArgumentException("Subject não pode ser nulo");
        }

        Leitor leitor = leitorGerenciador.findLeitorByEmail(subject);
      
        List<Livro> livros = (List<Livro>)livroRepo.findByLeitor(leitor);

        model.put("livros", livros);

        return model;
    }

}
