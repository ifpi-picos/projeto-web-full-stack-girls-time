package com.teamg.BookBee.controllers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import com.teamg.BookBee.model.Leitor;
import com.teamg.BookBee.repositorios.LeitorRepositorio;
import com.teamg.BookBee.service.CookieService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @Autowired
    private LeitorRepositorio repo;

    @PostMapping("/logar")
    public String logar(Model model, @RequestBody Leitor leitorParam, HttpServletResponse response) {
        Leitor leitor = this.repo.login(leitorParam.getEmail(), leitorParam.getSenha());
        if(leitor != null) {
            String nomeLeitor = URLEncoder.encode(String.valueOf(leitor.getNome()), StandardCharsets.UTF_8);
           

            CookieService.setCookie(response, "usuarioId", String.valueOf(leitor.getId()));
            CookieService.setCookie(response, "nomeLeitor", nomeLeitor);
            CookieService.setCookie(response, "usuarioNome", String.valueOf(leitor.getNomeUsuario()));

            model.addAttribute("nomeUsuario", leitor.getNomeUsuario());
            return "redirect:/livros/";
        }
        model.addAttribute("erro", "Usuario ou senha invalidos");
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logOut(HttpServletResponse response) {
        CookieService.deleteCookie(response, "usuarioId");
        CookieService.deleteCookie(response, "usuarioNome");
        CookieService.deleteCookie(response, "nomeLeitor");

        return "redirect:";
    }
}
