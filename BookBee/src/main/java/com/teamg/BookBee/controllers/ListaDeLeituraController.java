package com.teamg.BookBee.controllers;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.teamg.BookBee.configuracoes.TokenService;
import com.teamg.BookBee.gerenciadores.LeitorGerenciador;
import com.teamg.BookBee.gerenciadores.ListaDeLeituraGerenciador;
import com.teamg.BookBee.model.ListaDeLeitura;
import com.teamg.BookBee.service.CookieService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/listas")
public class ListaDeLeituraController {
    @Autowired
    private ListaDeLeituraGerenciador listaDeLeituraGerenciador;

    @Autowired
    private LeitorGerenciador leitorGerenciador;

    @Autowired
    private TokenService tokenService;

    @Operation(summary = "Cria uma nova lista de leitura e adiciona o livro", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description  = "Lista de leitura criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Solicitação inválida"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
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
                    return "erro/error404";
                }
            }
        }
        model.addAttribute("codigoErro", HttpServletResponse.SC_FORBIDDEN);
        return "error/error404";
    }

    @Operation(summary = "Adiciona um livro à lista de leitura existente", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description  = "Livro adicionado com sucesso à lista de leitura"),
        @ApiResponse(responseCode = "400", description = "Solicitação inválida"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
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
                    model.addAttribute("codigoErro", HttpServletResponse.SC_BAD_REQUEST);
                    model.addAttribute("erro", e.getMessage());
                    return "/erro/error404";
                }
            }
        }
        model.addAttribute("codigoErro", HttpServletResponse.SC_FORBIDDEN);
        return "error/error404";
    }

    @Operation(summary = "Exibe os livros de uma lista de leitura", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description  = "Livros da lista de leitura exibidos com sucesso"),
        @ApiResponse(responseCode = "404", description = "Lista de leitura não encontrada")
    })
    @GetMapping("/{listaId}")
    public String exibirLivrosDaLista(@PathVariable Long listaId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
    String token = CookieService.getCookie(request, "token");
    if(token != null){
        String subject = tokenService.validateToken(token);
        if(!subject.isEmpty()){
            Map<String, Object> leitorModel = 
                leitorGerenciador.getDadosDoUsuario(subject);
            model.addAllAttributes(leitorModel);
            model.addAttribute("nomeUsuario", CookieService.getCookie(request, "usuarioNome"));
            Map<String, Object> listas = 
                listaDeLeituraGerenciador.getListasDoUsuario(subject);
            model.addAllAttributes(listas);
            Map<String, Object> livrosListas = 
                listaDeLeituraGerenciador.buscarLivrosDaLista(listaId);
            model.addAllAttributes(livrosListas);
           
            response.setStatus(HttpServletResponse.SC_OK);
            return "/usuario/index";
            }
        }
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        model.addAttribute("codigoErro", HttpServletResponse.SC_NOT_FOUND);
        return "error/error404";
    }

}
