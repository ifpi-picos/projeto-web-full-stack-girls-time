package com.teamg.BookBee.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamg.BookBee.gerenciadores.LeitorGerenciador;
import com.teamg.BookBee.model.Leitor;


@Controller
public class LeitoresControllers {
    
    @Autowired
    private LeitorGerenciador leitorGerenciador;

    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastra(@RequestBody Leitor leitor, BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("redirect:/");
        }
        try {
            leitorGerenciador.cadastra(leitor);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("redirect:/");
        }
        return ResponseEntity.ok("redirect:/login");
    }
}
