package com.teamg.BookBee.service;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Serviço para manibulação de cookie HTTP.
 * Esta classe fornece métodos para criar, recuperar e deletar cookies HTTP.
 */
@Service
public class CookieService {

    /**
     * Criar um novo cookie e o adiciona à resposta.
     * 
     * @param response a resposta HTTP
     * @param key a chave do  cookie
     * @param valor o valor do cookie
     */
    public static void setCookie(HttpServletResponse response, String key, String valor) {
        Cookie cookie = new Cookie(key, valor);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    /**
     * Recupera o valor de um cookie HTTP a partir da requisição.
     * 
     * @param request a requisição HTTP
     * @param key a chave do cookie
     * @return o valor do cookie, ou null se o cookie não for encontrado
     */
    public static String getCookie(HttpServletRequest request, String key) {
        return Optional.ofNullable(request.getCookies())
        .flatMap(cookies -> Arrays.stream(cookies)
        .filter(cookie -> key.equals(cookie.getName()))
        .findAny()).map(e -> e.getValue())
        .orElse(null);
    }

    /**
     * Deleta um cookie HTTP definindo seu tempo de vida para zero
     * 
     * @param response a resposta HTTP
     * @param nome o nome do cookie
     */
    public static void deleteCookie(HttpServletResponse response, String nome) {
        Cookie cookie = new Cookie(nome, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

}
