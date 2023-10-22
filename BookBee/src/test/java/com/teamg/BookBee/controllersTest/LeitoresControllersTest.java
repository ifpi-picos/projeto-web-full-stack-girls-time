package com.teamg.BookBee.controllersTest;

import static org.junit.Assert.assertSame;

import java.util.stream.Collectors;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.teamg.BookBee.controllers.LeitoresControllers;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class LeitoresControllersTest{
  
    @LocalServerPort
    private int port;
    
    @Autowired
    private TestRestTemplate restRemplete;

    @Autowired
    private LeitoresControllers leitoresControllers;

    private MockMvc mockiMvc;


    @Before
    public void setUp() {
        this.mockiMvc = MockMvcBuilders.standaloneSetup(leitoresControllers).build();
    } 

    @Test
    public void testA_RestornaPaginaDeCadastro() {
        ResponseEntity<String> responseEntity = restRemplete.getForEntity(
            "http://localhost:"+ port +"/cadastro", 
            String.class);

            assertSame(HttpStatus.OK, responseEntity.getStatusCode());
    }
 
    @Test
    public void testB_CadastraLeitorComSucesso() throws Exception{
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("nome", "testeComSucesso");
        map.add("email", "testeComSucesso@gmail.com");
        map.add("senha", "senha");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
         String urlEncodedForm = request.getBody().toSingleValueMap()
            .entrySet().stream()
            .map(entry -> entry.getKey() + "=" + entry.getValue())
            .collect(Collectors.joining("&"));

        this.mockiMvc.perform(MockMvcRequestBuilders.post("/cadastra")
            .content(urlEncodedForm)
            .headers(headers)
            ).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.header()
                .string("location", Matchers.containsString("/login")));

    }

    @Test
    public void testC_CadastroSemNome() throws Exception {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("nome", "");
        map.add("email", "testeSemNome@gmail.com");
        map.add("senha", "senha");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
         String urlEncodedForm = request.getBody().toSingleValueMap()
            .entrySet().stream()
            .map(entry -> entry.getKey() + "=" + entry.getValue())
            .collect(Collectors.joining("&"));

        this.mockiMvc.perform(MockMvcRequestBuilders.post("/cadastra")
            .content(urlEncodedForm)
            .headers(headers)
            ).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.header()
                .string("location", Matchers.containsString("/")));
    }

    @Test
    public void testD_CadastroEmailRepetido() throws Exception {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("nome", "testeEmailrepetido");
        map.add("email", "testeComSucesso@gmail.com");
        map.add("senha", "senha");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
         String urlEncodedForm = request.getBody().toSingleValueMap()
            .entrySet().stream()
            .map(entry -> entry.getKey() + "=" + entry.getValue())
            .collect(Collectors.joining("&"));

        this.mockiMvc.perform(MockMvcRequestBuilders.post("/cadastra")
            .content(urlEncodedForm)
            .headers(headers)
            ).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.header()
                .string("location", Matchers.containsString("/")));
    }

    @Test
    public void testE_CadastroSemSenha() throws Exception {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("nome", "testeSemSenha");
        map.add("email", "testeSemSenah@gmail.comm");
        map.add("senha", "");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
         String urlEncodedForm = request.getBody().toSingleValueMap()
            .entrySet().stream()
            .map(entry -> entry.getKey() + "=" + entry.getValue())
            .collect(Collectors.joining("&"));

        this.mockiMvc.perform(MockMvcRequestBuilders.post("/cadastra")
            .content(urlEncodedForm)
            .headers(headers)
            ).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.header()
                .string("location", Matchers.containsString("/")));
    
        }
}
