package com.teamg.BookBee.gerenciadoresTest;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.teamg.BookBee.gerenciadores.LeitorGerenciador;
import com.teamg.BookBee.model.Leitor;
import com.teamg.BookBee.repositorios.LeitorRepositorio;

@SpringBootTest
public class LeitorGereenciardorTest {
     @InjectMocks
     private LeitorGerenciador leitorGerenciador;

     @Mock
     private LeitorRepositorio repo;
     
     
    @Test
    public void testCadastra() {
        Leitor leitor = new Leitor();
        leitor.setNome("Teste");
        leitor.setEmail("teste@teste.com");
        leitor.setSenha("senha");

        Mockito.when(repo.findByNomeUsuario(leitor.getNome())).thenReturn(null);
        Mockito.when(repo.findByEmail(leitor.getUsername())).thenReturn(null);

        leitorGerenciador.cadastra(leitor);

        Mockito.verify(repo, Mockito.times(1)).save(leitor);
    }

    @Test
    public void testCadastraNomeUsuarioExistente() {
        Leitor leitor = new Leitor();
        leitor.setNome("Teste");
        leitor.setEmail("teste@teste.com");
        leitor.setSenha("senha");

        Mockito.when(repo.findByNomeUsuario(leitor.getNome())).thenReturn(new Leitor());

        assertThrows(IllegalArgumentException.class, () -> {
            leitorGerenciador.cadastra(leitor);
        });
    }

    @Test
    public void testCadastraEmailExistente() {
        Leitor leitor = new Leitor();
        leitor.setNome("Teste");
        leitor.setEmail("teste@teste.com");
        leitor.setSenha("senha");

        Mockito.when(repo.findByNomeUsuario(leitor.getNome())).thenReturn(null);
        Mockito.when(repo.findByEmail(leitor.getUsername())).thenReturn(new Leitor());

        assertThrows(IllegalArgumentException.class, () -> {
            leitorGerenciador.cadastra(leitor);
        });
    }

    @Test
    public void testCadastraNomeUsuarioOuEmailVazio() {
        Leitor leitor = new Leitor();
        leitor.setNome("");
        leitor.setEmail("");
        leitor.setSenha("senha");
    
        assertThrows(IllegalArgumentException.class, () -> {
            leitorGerenciador.cadastra(leitor);
        });
    }

        
    @Test
    public void testCadastraSenhaCriptografada() {
        Leitor leitor = new Leitor();
        leitor.setNome("Teste");
        leitor.setEmail("teste@teste.com");
        leitor.setSenha("senha");
    
        Mockito.when(repo.findByNomeUsuario(leitor.getNome())).thenReturn(null);
        Mockito.when(repo.findByEmail(leitor.getUsername())).thenReturn(null);
    
        ArgumentCaptor<Leitor> argumentCaptor = ArgumentCaptor.forClass(Leitor.class);
        
        leitorGerenciador.cadastra(leitor);
    
        Mockito.verify(repo, Mockito.times(1)).save(argumentCaptor.capture());
        
        Leitor captorValue = argumentCaptor.getValue();
        
        assertNotEquals("senha", captorValue.getPassword());
    }
         
}
