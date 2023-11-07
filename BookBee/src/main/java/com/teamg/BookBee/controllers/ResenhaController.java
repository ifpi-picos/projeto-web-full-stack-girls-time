package com.teamg.BookBee.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.teamg.BookBee.configuracoes.TokenService;
import com.teamg.BookBee.gerenciadores.ResenhaGerenciador;
import com.teamg.BookBee.model.Resenha;
import com.teamg.BookBee.service.CookieService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ResenhaController {
    @Autowired 
    private ResenhaGerenciador resenhaGerenciador;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/resenha")
    public String salvarResenha(@ModelAttribute Resenha resenha, @RequestBody Resenha resenhaTxt, @RequestParam Long idLivro, @RequestParam Long idLeitor, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String token = CookieService.getCookie(request, "token");
        if(token != null){
            var subject = tokenService.validateToken(token);
            if(!subject.isEmpty()){
                resenhaGerenciador.criarResenha(resenhaTxt, idLivro, subject);
                return "redirect:/livros/" + idLivro;
            }
        }
        return "redirect:/error/403";
    }

}
