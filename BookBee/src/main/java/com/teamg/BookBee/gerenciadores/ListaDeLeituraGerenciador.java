package com.teamg.BookBee.gerenciadores;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamg.BookBee.model.Leitor;
import com.teamg.BookBee.model.ListaDeLeitura;
import com.teamg.BookBee.model.Livro;
import com.teamg.BookBee.repositorios.ListaDeLeituraRepositorio;

@Service
public class ListaDeLeituraGerenciador {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListaDeLeituraGerenciador.class);

    @Autowired
    private LivroGerenciador livroGerenciador;

    @Autowired
    private LeitorGerenciador leitorGerenciador;

    @Autowired
    private ListaDeLeituraRepositorio repo;

    public ListaDeLeitura criarLista(String nomeLista, Long idLivro, String email) throws Exception {
        LOGGER.info("Criando lista de leitura com o nome: {} para o email: {}", nomeLista, email);

         if(nomeLista == null || nomeLista.trim().isEmpty()){
            throw new IllegalArgumentException("Nome da lista de leitura é obrigatório");
         }

         if(nomeLista.length() > 25){
            throw new IllegalArgumentException("O nome da lista de leitura deve ter no maximo 25 caracter");
         }

         if (nomeLista.length() < 5) {
            throw new IllegalArgumentException("O nome da lista de leitura deve ter no minimo 5 caracteres");
         }

         Leitor leitor = leitorGerenciador.findLeitorByEmail(email);
         Optional<Livro> livroOptinal = livroGerenciador.getLivro(idLivro);

         if(!livroOptinal.isPresent()) {   
            throw new IllegalArgumentException("Livro nao encontrado");
        }

        Livro livro = livroOptinal.get();
        ListaDeLeitura novaLista =  new  ListaDeLeitura(leitor, livro, nomeLista);

        repo.save(novaLista);

        LOGGER.info("Lista de leitura criada com sucesso com o nome: {} para o email: {}", nomeLista, email);
        return novaLista;
    }

    public Map<String, Object> getListasDoUsuario(String email) throws Exception{
        LOGGER.info("Buscando listas de leitura do usuario com o email: {}", email);
        Map<String, Object> model = new HashMap<>();
        Leitor leitor = leitorGerenciador.findLeitorByEmail(email);

       List<ListaDeLeitura> listas = repo.findAllByLeitorId(leitor.getId());
       model.put("listas", listas);
   
        return model;
    }

    public void adicionarLivro(Long idLivros, Long listaId, String subject) throws Exception {
        Optional<Livro> livroOptional = livroGerenciador.getLivro(idLivros);
        Optional<ListaDeLeitura> listaOptional = repo.findById(listaId);
    
        if(!livroOptional.isPresent()) {   
            throw new IllegalArgumentException("Livro não encontrado");
        }
    
        if(!listaOptional.isPresent()) {   
            throw new IllegalArgumentException("Lista não encontrada");
        }
    
        Livro livro = livroOptional.get();
        ListaDeLeitura lista = listaOptional.get();

        if(lista.getLivros().contains(livro)){
            throw new IllegalArgumentException("O livro nao pode ser adicionado por já está na lista");
        }
    
        lista.getLivros().add(livro);
        repo.save(lista);
    }
    
    
        
}