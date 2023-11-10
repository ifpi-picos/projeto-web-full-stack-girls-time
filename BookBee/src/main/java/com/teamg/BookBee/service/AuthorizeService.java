package com.teamg.BookBee.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.teamg.BookBee.gerenciadores.LeitorGerenciador;
import com.teamg.BookBee.model.Leitor;

@Service
public class AuthorizeService implements UserDetailsService{
    
    @Autowired
    private LeitorGerenciador leitorGerenciador;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizeService.class);

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       Leitor leitor;
        try {
            LOGGER.info("Carregando usuario pelo email: {}", email);
            leitor = leitorGerenciador.findLeitorByEmail(email);
            return leitor;
        } catch (Exception e) {
            LOGGER.error("Erro ao carregar usuario pelo email: {}", email, e);
            throw new UsernameNotFoundException(email + "Nao encontrado");
        }
    }

}
