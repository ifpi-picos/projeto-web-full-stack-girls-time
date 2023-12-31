package com.teamg.BookBee.controllers;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.teamg.BookBee.configuracoes.TokenService;
import com.teamg.BookBee.gerenciadores.LeitorGerenciador;
import com.teamg.BookBee.gerenciadores.ListaDeLeituraGerenciador;
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
    private LivroGerenciador livroGerenciador;

    @Autowired
    private LeitorGerenciador leitorGerenciador;

    @Autowired
    private ListaDeLeituraGerenciador listaDeLeituraGerenciador;

    @Autowired
    private TokenService tokenService;

    @Operation(summary = "Obtém todos os livros e anotações do usuário para a pagina inicial", method = "GET")
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
                Map<String, Object> livroModel = livroGerenciador.getLivrosEAnotacoes(subject);
                model.addAllAttributes(livroModel);
                model.addAttribute("nomeUsuario", CookieService.getCookie(request, "usuarioNome"));
                response.setStatus(HttpServletResponse.SC_OK);
                return   "livros/index";
            }
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        model.addAttribute("codigoErro", HttpServletResponse.SC_FORBIDDEN);
        return "erro/error404";
    }

    @Operation(summary = "Exibe todos os livros do usuário", method = "GET")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description  = "Exibição bem-sucedida de todos os livros"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
        })
    @GetMapping("/todos-os-livros")
    public String exibirTodosOslivros(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception{
        String token = CookieService.getCookie(request, "token");
        if(token != null){
            var subject = tokenService.validateToken(token);
            if(!subject.isEmpty()){
                try {
                    Map<String, Object> livroModel = livroGerenciador.getTodosOsLivros(subject);
                    model.addAllAttributes(livroModel);
                    model.addAttribute("nomeUsuario", CookieService.getCookie(request, "usuarioNome"));
                    model.addAttribute("titulo", "Todos Os Livros");
                    response.setStatus(HttpServletResponse.SC_OK);
                    return "livros/metas"; 
                } catch (Exception e) {
                    model.addAttribute("nomeUsuario", CookieService.getCookie(request, "usuarioNome"));
                    model.addAttribute("titulo", "Todos Os Livros");
                    model.addAttribute("Exception", e.getMessage());
                    return "livros/metas";
                }
            }
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        model.addAttribute("codigoErro", HttpServletResponse.SC_FORBIDDEN);
        return "erro/error404";
    }

    @Operation(summary = "Exibe os detalhes de um livro especifico", method = "GET")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description  = "Exibição bem-sucedida dos detalhes do livros"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
        })
    @GetMapping("/{id}")
    public String detalhe(@PathVariable Long id, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String token = CookieService.getCookie(request, "token");
        
        if(token != null){
            var subject = tokenService.validateToken(token);
            if(!subject.isEmpty()){
                Map<String, Object> livroNotaEResenhaModel = livroGerenciador.getNotasEResenhaDoLivro(id, subject);
                model.addAttribute("nomeUsuario", CookieService.getCookie(request, "usuarioNome"));
                Map<String, Object> listas = listaDeLeituraGerenciador.getListasDoUsuario(subject);
                    double velocidadeLeitura = leitorGerenciador.calcularVelocidade(id, subject);
                    model.addAttribute("velocidade", velocidadeLeitura );
                if(livroNotaEResenhaModel.size() != 0){
                    if(listas.size() != 0){
                        model.addAllAttributes(listas);
                    }
                    model.addAllAttributes(livroNotaEResenhaModel);
                    response.setStatus(HttpServletResponse.SC_OK);
                    return  "livros/detalhesdolivro";
                }
            }
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        model.addAttribute("codigoErro", HttpServletResponse.SC_FORBIDDEN);
        return "erro/error404";
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
                model.addAttribute("nomeUsuario", CookieService.getCookie(request, "usuarioNome"));
                response.setStatus(HttpServletResponse.SC_OK);
                return "livros/busca";
            }
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        model.addAttribute("codigoErro", HttpServletResponse.SC_FORBIDDEN);
        return "erro/error404";
    }

    @Operation(summary = "Adiciona um novo livro", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description  = "Adição bem-sucedida do livro"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping("/adicionar")
    public String adicionar(@RequestBody Livro livro, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{
        String token = CookieService.getCookie(request, "token");
        if(token != null){
            var subject = tokenService.validateToken(token);
            livroGerenciador.adicionar(livro, subject);
            response.setStatus(HttpServletResponse.SC_OK);
            return   "redirect:/livros";
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        model.addAttribute("codigoErro", HttpServletResponse.SC_FORBIDDEN);
        return "erro/error404";
    }

    @Operation(summary = "Atualiza a data de início de um livro específico", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description  = "Atualização bem-sucedida da data de início do livro"),
        @ApiResponse(responseCode = "400", description = "Solicitação inválida"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping("/{id}/atualizar-data-ini")
    public String atualizarDataInicio(@PathVariable Long id, 
                                        @RequestParam("dataDeIni") LocalDate dataDeIni, 
                                        HttpServletRequest request, 
                                        HttpServletResponse response,
                                        Model model) throws Exception {

        String token = CookieService.getCookie(request, "token");

        if(token != null){
            var subject = tokenService.validateToken(token);
            if(!subject.isEmpty()) {
                Optional<Livro> optionalLivro = livroGerenciador.getLivro(id);
                if(optionalLivro.isPresent() && (dataDeIni != null && !dataDeIni.isAfter(LocalDate.now()))){
                    try {
                        Livro livro = optionalLivro.get();
                        livroGerenciador.atualizarDataIni(livro, dataDeIni, subject);
                        response.setStatus(HttpServletResponse.SC_OK);
                        return   "redirect:/livros/" + id;
                        
                    } catch (IllegalArgumentException e) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        model.addAttribute("codigoErro", HttpServletResponse.SC_BAD_REQUEST);
                        model.addAttribute("erro", e.getMessage());
                        return "erro/error404";
                    }
                }
            }
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        model.addAttribute("codigoErro", HttpServletResponse.SC_FORBIDDEN);
        return "/erro/error404";
    }

    @Operation(summary = "Atualiza a data de fim de um livro específico", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description  = "Atualização bem-sucedida da data de fim do livro"),
        @ApiResponse(responseCode = "400", description = "Solicitação inválida"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping("/{id}/atualizar-data-fim")
    public String atualizarDataFim(@PathVariable Long id, 
                                    @RequestParam("dataDeFim") LocalDate dataFim,
                                    HttpServletRequest request,
                                    HttpServletResponse response, Model model) throws Exception {
        String token = CookieService.getCookie(request, "token");

        if(token != null){
            var subject = tokenService.validateToken(token);
            if(!subject.isEmpty()) {
                Optional<Livro> optionalLivro = livroGerenciador.getLivro(id);
                if(optionalLivro.isPresent() && (dataFim != null && !dataFim.isAfter(LocalDate.now()))){
                    try {
                        Livro livro = optionalLivro.get();
                        livroGerenciador.atualizarDataFim(livro, dataFim, subject);
                        response.setStatus(HttpServletResponse.SC_OK);
                        return   "redirect:/livros/" + id ;
                    } catch (IllegalArgumentException e) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        model.addAttribute("codigoErro", HttpServletResponse.SC_BAD_REQUEST);
                        model.addAttribute("erro", e.getMessage());
                        return   "erro/error404"; 
                    }
                }
            }
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        model.addAttribute("codigoErro", HttpServletResponse.SC_FORBIDDEN);
        return "erro/error404";
    }
    
    @Operation(summary = "Atualiza a posição de leitura de um livro", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description  = "Posição de leitura atualizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Solicitação inválida"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping("/atualizar-posicao-leitura")
    public String atualizarPosicaoLeitura(Model model, @ModelAttribute Livro livro, @RequestParam String paginasLidas, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String token = CookieService.getCookie(request, "token");
        if(token != null){
            var subject = tokenService.validateToken(token);
            if(!subject.isEmpty()){
                try{
                    int posicao = Integer.parseInt(paginasLidas);
                    livroGerenciador.atualizarPossicaoDeLeitura(livro, posicao);
                    return   "redirect:/livros/" + livro.getIdLivro();
                } catch(IllegalArgumentException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    model.addAttribute("codigoErro", HttpServletResponse.SC_BAD_REQUEST);
                    model.addAttribute("erro", e.getMessage());
                    return "erro/error404";
                    }
                }
            }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        model.addAttribute("codigoErro", HttpServletResponse.SC_FORBIDDEN);
        return "erro/error404";
    }

    @Operation(summary = "Atualiza a classificação de um livro", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description  = "Classificação do livro atualizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Solicitação inválida"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping("/atualizar-classificacao")
    public String atualizarCLassificacao(Model model, @ModelAttribute Livro livro, @RequestBody Map<String, String> requestBody, HttpServletRequest request, HttpServletResponse response){
        String token = CookieService.getCookie(request, "token");
        String idlivroString = requestBody.get("idLivro");
        Long idlivro = Long.parseLong(idlivroString);

        if(token != null){
            String subject = tokenService.validateToken(token);
            if(!subject.isEmpty()){
                try {
                    livroGerenciador.atualizarClassificacao(idlivro, requestBody.get("classificacao"));
                    return "redirect:/livros/" + idlivro;
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
        return "erro/erro404";
    }

    @Operation(summary = "Atualiza o status de favorito de um livro", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description  = "Status de favorito do livro atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Solicitação inválida"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping("/adiciona-favorito")
    public String atualizarFavorito(Model model, @ModelAttribute Livro livro, @RequestBody Map<String, String> requestBody, HttpServletRequest request, HttpServletResponse response){
        String token = CookieService.getCookie(request, "token");
        String idlivroString = requestBody.get("idLivro");
        Long idlivro = Long.parseLong(idlivroString);
        System.out.println(idlivro);
        
        if(token != null){
            String subject = tokenService.validateToken(token);
            if(!subject.isEmpty()){
                try {
                    livroGerenciador.atualizarFavorito(idlivro, requestBody.get("favorito"));
                    return "redirect:/livros/" + livro.getIdLivro();
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
        return "erro/erro404";
    }

    @Operation(summary = "Exibe os livros marcado com favoritos do usuário", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description  = "Livros favoritos exibidos com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/livros-favoritos")
    public String exibirLivrosFavoritos(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception{
        String token = CookieService.getCookie(request, "token");
        if(token != null){
            var subject = tokenService.validateToken(token);
            if(!subject.isEmpty()){
                try {
                    Map<String, Object> livroModel = livroGerenciador.getLivrosFavoritos(subject);
                    model.addAllAttributes(livroModel);
                    model.addAttribute("titulo", "Livros Favoritos");
                    model.addAttribute("nomeUsuario", CookieService.getCookie(request, "usuarioNome"));
                    response.setStatus(HttpServletResponse.SC_OK);
                    return "livros/metas";
                } catch (Exception e) {
                    model.addAttribute("nomeUsuario", CookieService.getCookie(request, "usuarioNome"));
                    model.addAttribute("titulo", "Livros Favoritos");
                    model.addAttribute("Exception", e.getMessage());
                    return "livros/metas";
                }
               
            }
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        model.addAttribute("codigoErro", HttpServletResponse.SC_FORBIDDEN);
        return "erro/error404";
    }

    @Operation(summary = "Exibe os livros que nao foram concluidos ou iniciados do usuário", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description  = "Livros em progresso exibidos com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/meta-de-leitura")
    public String exibirLivrosEmProgresso(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception{
        String token = CookieService.getCookie(request, "token");
        if(token != null){
            var subject = tokenService.validateToken(token);
            if(!subject.isEmpty()){
                try {
                    Map<String, Object> livroModel = livroGerenciador.getLivrosEmProgresso(subject);
                    model.addAllAttributes(livroModel);
                    model.addAttribute("titulo", "Meta de leitura");
                    model.addAttribute("nomeUsuario", CookieService.getCookie(request, "usuarioNome"));
                    response.setStatus(HttpServletResponse.SC_OK);
                    return "livros/metas";
                } catch (Exception e) {
                    model.addAttribute("nomeUsuario", CookieService.getCookie(request, "usuarioNome"));
                    model.addAttribute("titulo", "Meta de leitura");
                    model.addAttribute("Exception", e.getMessage());
                    return "livros/metas";
                }
            }
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        model.addAttribute("codigoErro", HttpServletResponse.SC_FORBIDDEN);
        return "erro/error404";
    }

}
