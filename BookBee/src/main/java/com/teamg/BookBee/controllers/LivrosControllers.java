package com.teamg.BookBee.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamg.BookBee.gerenciadores.LivroGerenciador;
import com.teamg.BookBee.service.CookieService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("livros")
public class LivrosControllers {

    @Autowired
    private LivroGerenciador livroGereciador;

    @GetMapping
    public String index(Model model, HttpServletRequest request) {
        String usuarioId = CookieService.getCookie(request, "usuarioId");
        if(usuarioId != null){
            Map<String, Object> livroModel = livroGereciador.getLivrosAnotacoes(usuarioId);
            model.addAllAttributes(livroModel);
            model.addAttribute("nomeUsuario", CookieService.getCookie(request, "nomeUsuario"));
            return "livros/index";
        }
        return "redirect:";
    }

    @GetMapping("/{id}/detalhes")
    public String detalhe(@PathVariable Long id, Model model, HttpServletRequest request) {
        String nomeUsuario = CookieService.getCookie(request, "nomeUsuario");
        model.addAttribute("nomeUsuario", nomeUsuario);
        Map<String, Object> livroEAnotacaoModel = livroGereciador.getAnotacoesDoLivro(id, Long.parseLong( CookieService.getCookie(request, "usuarioId")));
        if(livroEAnotacaoModel.size() != 0){
            model.addAllAttributes(livroEAnotacaoModel);
            return "livros/detalhedolivro";
        }
        return "redirect:/error/404";
    }
}
