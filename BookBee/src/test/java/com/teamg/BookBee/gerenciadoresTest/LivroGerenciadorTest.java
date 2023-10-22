package com.teamg.BookBee.gerenciadoresTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.teamg.BookBee.gerenciadores.LivroGerenciador;
import com.teamg.BookBee.model.Leitor;
import com.teamg.BookBee.model.Livro;
import com.teamg.BookBee.repositorios.AnotacoesRepositorio;
import com.teamg.BookBee.repositorios.LeitorRepositorio;
import com.teamg.BookBee.repositorios.LivroRepositorio;

@SpringBootTest
public class LivroGerenciadorTest {
    
    @InjectMocks
    private LivroGerenciador livroGerenciador;

    @Mock
    private LivroRepositorio livroRepo;

    @Mock
    private AnotacoesRepositorio anotacoesRepo;

    @Mock
    private LeitorRepositorio leitorRepo;

    @Test
    public void testGetLivrosEAnotacoes() {
        Leitor leitor = new Leitor();
        leitor.setNome("Teste");
        leitor.setEmail("teste@teste.com");
        leitor.setSenha("senha");
        when(leitorRepo.findByEmail(anyString())).thenReturn(leitor);
        // Map<String, Object> livros = livroGerenciador.getLivrosEAnotacoes(leitor.getEmail());

        verify(livroRepo, times(1)).findByLeitor(leitor);
        verify(anotacoesRepo, times(1)).findByLeitor(leitor);
    }

    @Test
    public void testGetLivro() {
        Livro livro = new Livro();
        livro.setIdLivro(1L);

        when(livroRepo.findById(1L)).thenReturn(Optional.of(livro));
        Optional<Livro> resultado = livroGerenciador.getLivro(1L);
        assertTrue(resultado.isPresent());
        assertEquals(livro, resultado.get());

        verify(livroRepo, times(1)).findById(1L);

    }
}
