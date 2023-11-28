package com.teamg.BookBee.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.teamg.BookBee.configuracoes.TokenService;
import com.teamg.BookBee.gerenciadores.ResenhaGerenciador;
import com.teamg.BookBee.model.Resenha;
import com.teamg.BookBee.service.CookieService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/resenha")
public class ResenhaController {
    @Autowired 
    private ResenhaGerenciador resenhaGerenciador;

    @Autowired
    private TokenService tokenService;

    @Operation(summary = "Cria ou atualiza a resenha de um livro", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description  = "Resenha criada ou atualizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Solicitação inválida"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping("/criar-resenha")
    public String salvarResenha(@ModelAttribute Resenha resenha, @RequestParam String resenhatxt, @RequestParam Long idLivro, @RequestParam Long idLeitor, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String token = CookieService.getCookie(request, "token");
        if(token != null){
            var subject = tokenService.validateToken(token);
            if(!subject.isEmpty()){
                try {
                    resenhaGerenciador.criarOuAtualizarResenha(resenhatxt, idLivro, subject);
                    return "redirect:/livros/" + idLivro;
                } catch (IllegalArgumentException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    model.addAttribute("codigoErro", HttpServletResponse.SC_BAD_REQUEST);
                    model.addAttribute("erro", e.getMessage());
                    return "erro/error404";
                }
            }
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        model.addAttribute("codigoErro", HttpServletResponse.SC_FORBIDDEN);
        return "error/error404";
    }

}
