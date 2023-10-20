package com.teamg.BookBee.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.teamg.BookBee.gerenciadores.LeitorGerenciador;
import com.teamg.BookBee.model.Leitor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;


@Controller
public class LeitoresControllers {

    @Autowired
    private LeitorGerenciador leitorGerenciador;

    @Operation(summary = "Exibir a pagina de cadastro", method = "GET")
    @GetMapping("/cadastro")
    public String retornaPaginaDeCadastro(){
        return "cadastro";
    }

    @Operation(summary = "Cadastra um novo leitor", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description  = "Cadastro bem-sucedido"),
        @ApiResponse(responseCode = "400", description = "Erro no cadastro")
    })
    @PostMapping("/cadastra")
    public void cadastra(@ModelAttribute Leitor leitor, 
                            BindingResult bindingResult, 
                            HttpServletResponse response) throws IOException {
        if(bindingResult.hasErrors()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendRedirect("/");
            return;
        }
        try {
            leitorGerenciador.cadastra(leitor);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendRedirect("/");
            return;
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect("/login");
    }

}
