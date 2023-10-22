package com.teamg.BookBee.controllersTest;

import static org.junit.Assert.assertSame;

import java.util.stream.Collectors;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.teamg.BookBee.controllers.LoginController;
import com.teamg.BookBee.model.Leitor;
import com.teamg.BookBee.repositorios.LeitorRepositorio;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class LoginControllerTest {
    
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private LoginController loginController;

    @Autowired
    private LeitorRepositorio leitorRepositorio;

    private MockMvc mockiMvc;

    @Before
    public void setUp() {
        this.mockiMvc = MockMvcBuilders.standaloneSetup(loginController).build();
    }

    @Test
    public void testA_RetornandoPaginaDeLogin() {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/login", String.class);

        assertSame(HttpStatus.OK, response.getStatusCode());
    }

    public void iniciarUmLeitor() {
           Leitor leitorTeste  = new Leitor();
        leitorTeste.setNome("testeLoginComSucesso");
        leitorTeste.setEmail("loginComSucesso@gmail.com");
        leitorTeste.setSenha("senha");

        leitorRepositorio.save(leitorTeste);
    }

    @Test
    public void testB_LoginComSucesso() throws Exception{
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("email", "testeComSucesso@gmail.com");
            map.add("senha", "senha");

            
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
         String urlEncodedForm = request.getBody().toSingleValueMap()
            .entrySet().stream()
            .map(entry -> entry.getKey() + "=" + entry.getValue())
            .collect(Collectors.joining("&"));


            
        this.mockiMvc.perform(MockMvcRequestBuilders.post("/logar")
            .content(urlEncodedForm)
            .headers(headers)
            ).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.header()
                .string("location", Matchers.containsString("/livros")))
            .andExpect(MockMvcResultMatchers.cookie().exists("token"))
            .andExpect(MockMvcResultMatchers.cookie().exists("usuarioNome"));
        }

    @Test
    public void testB_LoginFalhaNaAutenticacao() throws Exception{
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("email", "testeComSucesso@gmail.com");
            map.add("senha", "sEnha");

            
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
         String urlEncodedForm = request.getBody().toSingleValueMap()
            .entrySet().stream()
            .map(entry -> entry.getKey() + "=" + entry.getValue())
            .collect(Collectors.joining("&"));


            
        this.mockiMvc.perform(MockMvcRequestBuilders.post("/logar")
            .content(urlEncodedForm)
            .headers(headers)
            ).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.header()
                .string("location", Matchers.containsString("/")))
            .andExpect(MockMvcResultMatchers.cookie().doesNotExist("token"))
            .andExpect(MockMvcResultMatchers.cookie().doesNotExist("usuarioNome"));
        }

    @Test
    public void testC_LoginFalhaNaAutenticacao() throws Exception{
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("email", "testeComSucessO@gmail.com");
            map.add("senha", "senha");

            
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
         String urlEncodedForm = request.getBody().toSingleValueMap()
            .entrySet().stream()
            .map(entry -> entry.getKey() + "=" + entry.getValue())
            .collect(Collectors.joining("&"));


            
        this.mockiMvc.perform(MockMvcRequestBuilders.post("/logar")
            .content(urlEncodedForm)
            .headers(headers)
            ).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.header()
                .string("location", Matchers.containsString("/")))
            .andExpect(MockMvcResultMatchers.cookie().doesNotExist("token"))
            .andExpect(MockMvcResultMatchers.cookie().doesNotExist("usuarioNome"));
        }

}
