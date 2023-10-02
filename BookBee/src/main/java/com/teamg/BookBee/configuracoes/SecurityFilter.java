package com.teamg.BookBee.configuracoes;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.teamg.BookBee.model.Leitor;
import com.teamg.BookBee.repositorios.LeitorRepositorio;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private LeitorRepositorio leitorRepositorio;

    @Override
    protected void doFilterInternal(HttpServletRequest request,         
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
       
        var token = this.recoverToken(request);
        if(token != null){
            String login = tokenService.validateToke(token);
            Leitor leitor = leitorRepositorio.findByEmail(login);

            var authentication = new UsernamePasswordAuthenticationToken(leitor.getUsername(), leitor.getPassword(), leitor.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
    
}
