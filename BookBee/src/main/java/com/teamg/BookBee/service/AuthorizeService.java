package com.teamg.BookBee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.teamg.BookBee.repositorios.LeitorRepositorio;

/**
 * Serviço para autorização de usuarios.
 * Esta classe implementa a interface UserDetailsService do Spring Security para fornecer
 * detalhes do usuário a partir de um nome de usuário(neste caso, email).
 */
@Service
public class AuthorizeService implements UserDetailsService{
    
    /**
     * Repositório para acesso aos dados dos leitores.
     */
    @Autowired
    private LeitorRepositorio leitorRepositorio;

    /** 
     * Carrega os detalhes do usuário pelo email.
     * 
     * @param email o email do usuário
     * @return os detalhes do usuário
     * @throws UsernameNotFoundException se o usuário não for encontrado
    */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return leitorRepositorio.findByEmail(email);
    }

}
