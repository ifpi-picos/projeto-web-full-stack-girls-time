package com.teamg.BookBee.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import com.teamg.BookBee.configuracoes.TokenService;
import com.teamg.BookBee.gerenciadores.LeitorGerenciador;
import com.teamg.BookBee.gerenciadores.LivroGerenciador;
import com.teamg.BookBee.model.Leitor;
import com.teamg.BookBee.model.Livro;
import com.teamg.BookBee.service.CookieService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Controller
public class LeitoresControllers {

    @Autowired
    private LeitorGerenciador leitorGerenciador;

    @Autowired 
    private LivroGerenciador livroGerenciador;

    @Autowired
    private TokenService tokenService;

    @Operation(summary = "Exibir a pagina de cadastro", method = "GET")
    @GetMapping("/cadastro")
    public String retornaPaginaDeCadastro(){
        return "cadastro";
    }

    @Operation(summary = "Cadastra um novo leitor", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description  = "Cadastro bem-sucedido"),
        @ApiResponse(responseCode = "400", description = "Erro no cadastro")
    })
    @PostMapping("/cadastra")
    public String cadastra(@ModelAttribute Leitor leitor, 
                            BindingResult bindingResult,
                            Model model,
                            HttpServletResponse response) throws IOException {
        if(bindingResult.hasErrors()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute("erro", "Erro de Validação");
            return "/cadastro";
        
        }
        try {
            leitorGerenciador.cadastra(leitor);
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute("erro", e.getMessage());
            return "/cadastro";
        }
        response.setStatus(HttpServletResponse.SC_CREATED);
        return"redirect:/login";
    }

    @PostMapping("/atualizarPosicaoLeitura")
    public String atualizarPosicaoLeitura(Model model, @RequestBody Livro livro, @RequestParam int paginasLidas, HttpServletRequest request, HttpServletResponse response) {
        String token = CookieService.getCookie(request, "token");
        if(token != null){
            var subject = tokenService.validateToken(token);
            if(!subject.isEmpty()){
                try{
                    boolean isLivroDoUsuario = leitorGerenciador.livroDoUsuario(livro, token);
                    if (!isLivroDoUsuario) {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        return "redirect:/livros";
                    }
                    livroGerenciador.atualizarPossicaoDeLeitura(livro, paginasLidas);
                    double velocidadeLeitura = leitorGerenciador.calcularVelocidade(livro, subject);
                    model.addAttribute("velocidade", velocidadeLeitura );

                    return   "redirect:/livros/" + livro.getIdLivro() + "/detalhes";
                } catch(Exception e) {
                    return "redirect:/erro";
                    }
                }
            }
        return "redirect:/login";
    }
}