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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/resenha")
public class ResenhaController {
    @Autowired 
    private ResenhaGerenciador resenhaGerenciador;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/criarResenha")
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
                    model.addAttribute("erro", e.getMessage());
                    return "/livros/error404";
                }
            }
        }
        return "redirect:/error/403";
    }

    @PostMapping("/deletarResenha")
    public String deletarResenha(@RequestParam Long idResenha, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String token = CookieService.getCookie(request, "token");
        if(token != null){
            var subject = tokenService.validateToken(token);
            if(!subject.isEmpty()){
                try {
                    resenhaGerenciador.deletarResenha(idResenha, subject);
                    return "redirect:/livros";
                } catch (Exception e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    model.addAttribute("erro", e.getMessage());
                    return "/livros/error404";
                }
            }
        }
        return "redirect:/error/403";
    }

}
