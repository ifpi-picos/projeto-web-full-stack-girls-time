package com.teamg.BookBee.controllers;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import com.teamg.BookBee.configuracoes.TokenService;
import com.teamg.BookBee.gerenciadores.LeitorGerenciador;
import com.teamg.BookBee.model.Leitor;
import com.teamg.BookBee.service.CookieService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Controller
public class LeitoresController {

    @Autowired
    private LeitorGerenciador leitorGerenciador;

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

    @GetMapping("usuario/pagina-do-usuario")
    public String exibirPaginaDoUsuario(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception{
        String token = CookieService.getCookie(request, "token");
        if(token != null){
            var subject = tokenService.validateToken(token);
            if(!subject.isEmpty()){
                Map<String, Object> leitorModel = 
                    leitorGerenciador.getDadosDoUsuario(subject);
                    model.addAllAttributes(leitorModel);
                    model.addAttribute("nomeUsuario", CookieService.getCookie(request, "usuarioNome"));
                response.setStatus(HttpServletResponse.SC_OK);
                return "/usuario/index";
            }
        }
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return "/erro/error404";
    }

    @GetMapping("usuario/configuracoes")
    public String exibirConfiguracoes(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
    String token = CookieService.getCookie(request, "token");
        if(token != null){
            var subject = tokenService.validateToken(token);
            if(!subject.isEmpty()){
                Map<String, Object> leitorModel = 
                    leitorGerenciador.getDadosDoUsuario(subject);
                    model.addAllAttributes(leitorModel);
                    model.addAttribute("nomeUsuario", CookieService.getCookie(request, "usuarioNome"));
                response.setStatus(HttpServletResponse.SC_OK);
                return "/usuario/confUsuario";
            }
        }
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return "/erro/error404";
    }

    @PostMapping("usuario/atualiza")
    public String atualiza(@ModelAttribute Leitor leitor, 
                        @RequestParam String emailAtual,
                        BindingResult bindingResult,
                        Model model,
                        HttpServletResponse response) throws IOException {
        if(bindingResult.hasErrors()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute("erro", "Erro de Validação");
            return "/usuario/confUsuario";
        }
        try {
            leitorGerenciador.atualiza(leitor, emailAtual);
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute("erro", e.getMessage());
            return "/usuario/confUsuario";
        }
        response.setStatus(HttpServletResponse.SC_OK);
        return "redirect:/usuario/configuracoes";
    }


}