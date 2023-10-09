package com.teamg.BookBee.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamg.BookBee.configuracoes.TokenService;
import com.teamg.BookBee.gerenciadores.LivroGerenciador;
import com.teamg.BookBee.model.Livro;
import com.teamg.BookBee.service.CookieService;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("livros")
public class LivrosControllers {
    
    @Autowired
    private LivroGerenciador livroGereciador;

    @Autowired
    private TokenService tokenService;

    @GetMapping
    public ResponseEntity<String> index(Model model, HttpServletRequest request) {
        String token = CookieService.getCookie(request, "token");
        if(token != null){
            var subject = tokenService.validateToken(token);
            if(!subject.isEmpty()){

                Map<String, Object> livroModel = livroGereciador.getLivrosAnotacoes(subject);
                model.addAllAttributes(livroModel);
                model.addAttribute("nomeUsuario", CookieService.getCookie(request, "nomeUsuario"));
                return ResponseEntity.ok("livros/index");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("redirect:/");
    }

    @GetMapping("/{id}/detalhes")
    public ResponseEntity<String> detalhe(@PathVariable Long id, Model model, HttpServletRequest request) {
        String token = CookieService.getCookie(request, "token");
        
        if(token != null){
            var subject = tokenService.validateToken(token);
            if(!subject.isEmpty()){
                Map<String, Object> livroEAnotacaoModel = livroGereciador.getAnotacoesDoLivro(id, subject);
                model.addAttribute("nomeUsuario", CookieService.getCookie(request, "nomeUsuario"));
                if(livroEAnotacaoModel.size() != 0){
                    model.addAllAttributes(livroEAnotacaoModel);
                    return ResponseEntity.ok("livros/detalhedolivro");
                }
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("redirect:/error/404");
    }

    @PostMapping("/adicionar")
    public ResponseEntity<String> adicionar(@RequestBody Livro livro, HttpServletRequest request){
        String token = CookieService.getCookie(request, "token");
        if(token != null){
            var subject = tokenService.validateToken(token);
            livroGereciador.adicionar(livro, subject);
            return ResponseEntity.ok("redirect:/livros");

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("redirect:/error/404");
    }
}
