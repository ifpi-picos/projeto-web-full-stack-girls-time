package com.teamg.BookBee.controllers;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.teamg.BookBee.configuracoes.TokenService;
import com.teamg.BookBee.gerenciadores.LeitorGerenciador;
import com.teamg.BookBee.model.Leitor;
import com.teamg.BookBee.service.CookieService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private LeitorGerenciador leitorGerenciador;

    @Autowired
    private AuthenticationManager authenticationManeger;

    @Autowired
    private TokenService tokenService;

    @Operation(summary = "Exibir a pagina de Login", method = "POST")
    @GetMapping("/login")
    public String showloginPage(){
        return "login";
    }

    @Operation(summary = "Autendtica o usuario e redireciona para a pagina de livros se bem sucedido", method = "POST")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "201", description = "Login realizado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Usuario ou Senha Invalidos")
        }
    )
    @PostMapping("/logar")
    public String logar(Model model, 
                        @ModelAttribute Leitor leitorParam, 
                        HttpServletResponse response) throws IOException {
        authenticationManeger = context.getBean(AuthenticationManager.class);
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(leitorParam.getUsername(), leitorParam.getPassword());
            var auth = this.authenticationManeger.authenticate(usernamePassword);
            String token = (String) tokenService.generateToken((Leitor) auth.getPrincipal());

            String subject = tokenService.validateToken(token);
     

            if(!subject.isEmpty()){
                Leitor leitor = this.leitorGerenciador.findLeitorByEmail(subject);

                CookieService.setCookie(response, "token", token);
                CookieService.setCookie(response, "usuarioNome", leitor.getNome());

                response.setStatus(HttpServletResponse.SC_CREATED);
             
                return "redirect:/livros/";
            }else {

                model.addAttribute("erro", "Ocorreu erro ao validar o usuario");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return "/login";
            }

        
        } catch(Exception e){
            model.addAttribute("erro", "Usuario ou senha invalidos");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return  "/login";

        }

    }

    @Operation(summary = "Faz logout do usuário", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description  = "Logout bem-sucedido")
    })
    @GetMapping("/logout")
    public String logOut(HttpServletResponse response) {
        
        CookieService.deleteCookie(response, "token");
        CookieService.deleteCookie(response, "usuarioNome");


        return "redirect:/";
    }
}
