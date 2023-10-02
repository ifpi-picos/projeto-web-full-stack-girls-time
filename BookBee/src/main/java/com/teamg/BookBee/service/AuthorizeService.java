package com.teamg.BookBee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.teamg.BookBee.repositorios.LeitorRepositorio;

@Service
public class AuthorizeService implements UserDetailsService{
    
    @Autowired
    private LeitorRepositorio leitorRepositorio;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return leitorRepositorio.findByEmail(email);
    }

}
