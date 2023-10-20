package com.teamg.BookBee.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.teamg.BookBee.configuracoes.TokenService;
import com.teamg.BookBee.gerenciadores.AnotacaoGerenciador;
import com.teamg.BookBee.service.CookieService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AnotacoesController {

    @Autowired
    private AnotacaoGerenciador anotacaoGerenciador;

    @Autowired
    private TokenService tokenService;

    @Operation(summary = "Obtém todas as anotações do usuário", method = "GET")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description  = "Obtenção bem-sucedida das anotações"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
        })
    @GetMapping("/anotacoes")
    public String index(Model model,
                        HttpServletRequest request,
                        HttpServletResponse response) throws Exception{

        String token = CookieService.getCookie(request, "token");
        if(token != null){
            var subject = tokenService.validateToken(token);
            if(!subject.isEmpty()){
                Map<String, Object> anotacoesModel = anotacaoGerenciador.findNotasByEmail(subject);
                model.addAllAttributes(anotacoesModel);
                model.addAttribute("nomeUsuario", CookieService.getCookie(request, "nomeUsuario"));
                response.setStatus(HttpServletResponse.SC_OK);
                return "anotacoes/index";
            }
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return "redirect:/error/403";
    }
}
