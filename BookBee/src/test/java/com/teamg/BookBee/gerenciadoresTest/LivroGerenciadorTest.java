package com.teamg.BookBee.gerenciadoresTest;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.teamg.BookBee.gerenciadores.LivroGerenciador;
import com.teamg.BookBee.model.Leitor;
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
    public void testGetLivrosAnotacoes() {
        Leitor leitor = new Leitor();
        leitor.setNome("Teste");
        leitor.setEmail("teste@teste.com");
        leitor.setSenha("senha");
        when(leitorRepo.findByEmail(anyString())).thenReturn(leitor);

        
        Map<String, Object> result = livroGerenciador.getLivrosAnotacoes("test@example.com");


        verify(livroRepo, times(1)).findByLeitor(leitor);
        verify(anotacoesRepo, times(1)).findByLeitor(leitor);
    }
}
