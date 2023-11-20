package com.teamg.BookBee.controllers;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.teamg.BookBee.configuracoes.TokenService;
import com.teamg.BookBee.gerenciadores.ListaDeLeituraGerenciador;
import com.teamg.BookBee.model.ListaDeLeitura;
import com.teamg.BookBee.service.CookieService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/listas")
public class ListaDeLeituraController {
    @Autowired
    private ListaDeLeituraGerenciador listaDeLeituraGerenciador;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/criarlista")
    public String criarLista(@RequestParam String nomeListatxt, @RequestParam Long idLivros, Model model, HttpServletResponse response, HttpServletRequest request) throws Exception {
        String token = CookieService.getCookie(request, "token");
        if(token != null){
            String subject = tokenService.validateToken(token);
            if(!subject.isEmpty()){
                try {
                    ListaDeLeitura novaLista = listaDeLeituraGerenciador.criarLista(nomeListatxt, idLivros, subject);
                    model.addAttribute("listaDeLivro", novaLista);
                    return "redirect:/livros/" + idLivros;
                } catch (IllegalArgumentException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    model.addAttribute("erro", e.getMessage());
                    return "/livros/error404";
                }
            }
        }
        return "redirect:/error/403";
    }

    @PostMapping("/adicionarLivro")
    public String adicionarLivro(@ModelAttribute ListaDeLeitura listaDeLeitura, @RequestParam Long idLivros, @RequestParam Long listaId, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String token = CookieService.getCookie(request, "token");
        if(token != null){
            String subject = tokenService.validateToken(token);
            if(!subject.isEmpty()){
                try {
                    listaDeLeituraGerenciador.adicionarLivro(idLivros, listaId, subject);
                    return "redirect:/livros/" + idLivros;
                } catch (IllegalArgumentException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    model.addAttribute("erro", e.getMessage());
                    return "/livros/error404";
                }
            }
        }
        return "redirect:/error/403";
    }


    @GetMapping("/minhaslistas")
    public String minhasListas(Model model, HttpServletRequest request) throws Exception {
    String token = CookieService.getCookie(request, "token");
    if(token != null){
        String subject = tokenService.validateToken(token);
        if(!subject.isEmpty()){
             Map<String, Object> listas = listaDeLeituraGerenciador.getListasDoUsuario(subject);
            model.addAllAttributes(listas);
            return "minhaslistas";
            }
        }
        return "redirect:/error/403";
    }

}
