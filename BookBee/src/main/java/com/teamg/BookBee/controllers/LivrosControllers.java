package com.teamg.BookBee.controllers;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.teamg.BookBee.configuracoes.TokenService;
import com.teamg.BookBee.gerenciadores.LivroGerenciador;
import com.teamg.BookBee.model.Livro;
import com.teamg.BookBee.service.CookieService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/livros")
public class LivrosControllers {
    
    @Autowired
    private LivroGerenciador livroGereciador;

    @Autowired
    private TokenService tokenService;

    @Operation(summary = "Obtém todos os livros e anotações do usuário", method = "GET")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description  = "Obtenção bem-sucedida dos livros e anotações"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
        })
    @GetMapping("/")
    public String index(Model model, 
                        HttpServletRequest request, 
                        HttpServletResponse response) throws Exception {
        String token = CookieService.getCookie(request, "token");
        if(token != null){
            var subject = tokenService.validateToken(token);
            if(!subject.isEmpty()){
                Map<String, Object> livroModel = livroGereciador.getLivrosEAnotacoes(subject);
                model.addAllAttributes(livroModel);
                model.addAttribute("nomeUsuario", CookieService.getCookie(request, "nomeUsuario"));
                response.setStatus(HttpServletResponse.SC_OK);
                return   "livros/index";
            }
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return "redirect:/error/403";
    }

    @Operation(summary = "Exibe todos os livros do usuário", method = "GET")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description  = "Exibição bem-sucedida de todos os livros"),
            @ApiResponse(responseCode = "404", description = "Livros não encontrados")
        })
    @GetMapping("/todos-os-livros")
    public String exibirTodosOslivros(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception{
         String token = CookieService.getCookie(request, "token");
        if(token != null){
            var subject = tokenService.validateToken(token);
            if(!subject.isEmpty()){
                Map<String, Object> livroModel = livroGereciador.getTodosOsLivros(subject);
                model.addAllAttributes(livroModel);
                model.addAttribute("nomeUsuario", CookieService.getCookie(request, "nomeUsuario"));
                response.setStatus(HttpServletResponse.SC_OK);
                return   "livros/lista";
            }
        }
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return "redirect:/error/404";
    }

    @Operation(summary = "Exibe os detalhes de um livro especifico", method = "GET")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description  = "Exibição bem-sucedida dos detalhes do livros"),
            @ApiResponse(responseCode = "404", description = "Livros não encontrados")
        })
    @GetMapping("/{id}/detalhes")
    public String detalhe(@PathVariable Long id, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String token = CookieService.getCookie(request, "token");
        
        if(token != null){
            var subject = tokenService.validateToken(token);
            if(!subject.isEmpty()){
                Map<String, Object> livroEAnotacaoModel = livroGereciador.getAnotacoesDoLivro(id, subject);
                model.addAttribute("nomeUsuario", CookieService.getCookie(request, "nomeUsuario"));
                if(livroEAnotacaoModel.size() != 0){
                    model.addAllAttributes(livroEAnotacaoModel);
                    response.setStatus(HttpServletResponse.SC_OK);
                    return  "livros/detalhedolivro";
                }
            }
        }

        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return "redirect:/error/404";
    }

    @Operation(summary = "Exibe a página de busca de livros", method = "GET")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description  = "Exibição bem-sucedida da página de busca"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
        })
    @GetMapping("/pagina-de-busca")
    public String paginaDeBusca(Model model, HttpServletRequest request, HttpServletResponse response ){
        String token = CookieService.getCookie(request, "token");
        
        if(token != null){
            var subject = tokenService.validateToken(token);
            if(!subject.isEmpty()){
                model.addAttribute("nomeUsuario", CookieService.getCookie(request, "nomeUsuario"));
                response.setStatus(HttpServletResponse.SC_OK);
                return "livros/paginabusca";
            }
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return "redirect:/error/403";
    }

    @Operation(summary = "Adiciona um novo livro", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description  = "Adição bem-sucedida do livro"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping("/adicionar")
    public String adicionar(@RequestBody Livro livro, HttpServletRequest request, HttpServletResponse response) throws Exception{
        String token = CookieService.getCookie(request, "token");
        if(token != null){
            var subject = tokenService.validateToken(token);
            livroGereciador.adicionar(livro, subject);
            response.setStatus(HttpServletResponse.SC_CREATED);
            return   "redirect:/livros";
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return "redirect:/error/403";
    }

    @Operation(summary = "Atualiza a data de início de um livro específico", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description  = "Atualização bem-sucedida da data de início do livro"),
        @ApiResponse(responseCode = "400", description = "Solicitação inválida"),
        @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    @PostMapping("/{id}/atualizar-data-ini")
    public String atualizarDataInicio(@PathVariable Long id, 
                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio, 
                                        HttpServletRequest request, 
                                        HttpServletResponse response) throws Exception {

        String token = CookieService.getCookie(request, "token");

        if(token != null){
            var subject = tokenService.validateToken(token);
            if(subject.isEmpty()) {
                Optional<Livro> optionalLivro = livroGereciador.getLivro(id);
                if(optionalLivro.isPresent() && (dataInicio != null && !dataInicio.isAfter(LocalDate.now()))){
                    Livro livro = optionalLivro.get();
                    livroGereciador.atualizarDataIni(livro, dataInicio, subject);
                    response.setStatus(HttpServletResponse.SC_OK);
                    return   "redirect:/livros/" + id + "/detalhes";
                }
                else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return   "redirect:/livros";
                }
    
            }
        }
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "redirect:/error/404";
    }

    @Operation(summary = "Atualiza a data de fim de um livro específico", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description  = "Atualização bem-sucedida da data de fim do livro"),
        @ApiResponse(responseCode = "400", description = "Solicitação inválida"),
        @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    @PostMapping("/{id}/atualizar-data-fim")
    public String atualizarDataFim(@PathVariable Long id, 
                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) throws Exception {
        String token = CookieService.getCookie(request, "token");

        if(token != null){
            var subject = tokenService.validateToken(token);
            if(subject.isEmpty()) {
                Optional<Livro> optionalLivro = livroGereciador.getLivro(id);
                if(optionalLivro.isPresent() && (dataFim != null && !dataFim.isAfter(LocalDate.now()))){
                    Livro livro = optionalLivro.get();
                    livroGereciador.atualizarDataFim(livro, dataFim, subject);
                      response.setStatus(HttpServletResponse.SC_OK);
                    return   "redirect:/livros/" + id + "/detalhes";
                }
                else {
                     response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return   "redirect:/livros";
                }
            }
        }
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return "redirect:/error/404";
    }
}
