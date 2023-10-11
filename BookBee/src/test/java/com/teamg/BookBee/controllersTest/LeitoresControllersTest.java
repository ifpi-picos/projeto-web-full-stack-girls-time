package com.teamg.BookBee.controllersTest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.teamg.BookBee.controllers.LeitoresControllers;
import com.teamg.BookBee.gerenciadores.LeitorGerenciador;
import com.teamg.BookBee.model.Leitor;

@WebMvcTest(LeitoresControllers.class)
public class LeitoresControllersTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LeitorGerenciador leitorGerenciador;

    @Test
    public void testCadastra() throws Exception {
        String leitorJson = "{\"nome\":\"Teste\",\"email\":\"teste@teste.com\"}";
    
        Leitor leitor = new Leitor();
        leitor.setNome("Teste");
        leitor.setEmail("teste@teste.com");
    
        Mockito.doNothing().when(leitorGerenciador).cadastra(Mockito.any(Leitor.class));
    
        mockMvc.perform(MockMvcRequestBuilders.post("/cadastro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(leitorJson))
                .andExpect(status().isOk());
    
        Mockito.verify(leitorGerenciador, Mockito.times(1)).cadastra(Mockito.any(Leitor.class));
    }

}
