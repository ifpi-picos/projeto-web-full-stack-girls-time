package com.teamg.BookBee.controllers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.teamg.BookBee.configuracoes.TokenService;
import com.teamg.BookBee.model.Leitor;
import com.teamg.BookBee.repositorios.LeitorRepositorio;
import com.teamg.BookBee.service.CookieService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @Autowired
    private LeitorRepositorio repo;

    @Autowired
    private AuthenticationManager authenticationManeger;

    @Autowired
    private TokenService tokenService;

    @GetMapping("login")
    public String showloginPage(){
        return "login";
    }

    @PostMapping("/logar")
    public ResponseEntity<String> logar(Model model, @RequestBody Leitor leitorParam, HttpServletResponse response) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(leitorParam.getUsername(), leitorParam.getPassword());
            var auth = this.authenticationManeger.authenticate(usernamePassword);

            String token = (String) tokenService.generateToken((Leitor) auth.getPrincipal());

            String subject = tokenService.validateToken(token);

            if(!subject.isEmpty()){
                Leitor leitor = this.repo.findByEmail(subject);

                CookieService.setCookie(response, "token", token);
                CookieService.setCookie(response, "nomeLeitor", leitor.getNome());
                CookieService.setCookie(response, "UsuarionNome", leitor.getNomeUsuario());

                model.addAttribute("nomeUsuario", leitor.getNomeUsuario());
                return ResponseEntity.ok("rediretct:/livros");
            }

            model.addAttribute("erro", "usuario ou senha invalidos");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("redirect:/");
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR
            ).body(e.getMessage());
        }

    }

    @GetMapping("/logout")
    public String logOut(HttpServletResponse response) {
        CookieService.deleteCookie(response, "token");
        CookieService.deleteCookie(response, "usuarioNome");
        CookieService.deleteCookie(response, "nomeLeitor");

        return "redirect:";
    }
}
