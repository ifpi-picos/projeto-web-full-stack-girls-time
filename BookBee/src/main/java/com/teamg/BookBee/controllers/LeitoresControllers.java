package com.teamg.BookBee.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamg.BookBee.gerenciadores.LeitorGerenciador;
import com.teamg.BookBee.model.Leitor;


@RestController
@RequestMapping("usuarios")
public class LeitoresControllers {
    
    @Autowired
    private LeitorGerenciador leitorGerenciador;

    @PostMapping("/cadastra")
    public String cadastra(@RequestBody Leitor leitor, BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            return "redirect:/";
        }
        try {
            leitorGerenciador.cadastra(leitor);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return "redirect:/";
        }
        return "redirect:/livros";
    }
}
