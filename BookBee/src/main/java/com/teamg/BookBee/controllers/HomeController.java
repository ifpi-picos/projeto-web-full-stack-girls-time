package com.teamg.BookBee.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Controller
public class HomeController {

    @Operation(summary = "Retorna a página inicial", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorno a página inicial")
    })
    @GetMapping("/")
    public String home(){
        return "home";
    }
}
