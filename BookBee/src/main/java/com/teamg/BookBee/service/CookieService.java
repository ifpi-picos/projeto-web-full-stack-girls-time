package com.teamg.BookBee.service;

import java.util.Arrays;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class CookieService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CookieService.class);

    public static void setCookie(HttpServletResponse response, String key, String valor) {
        LOGGER.info("Configurando cookie para a chave: {}", key);
        Cookie cookie = new Cookie(key, valor);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public static String getCookie(HttpServletRequest request, String key) {
        LOGGER.info("Obtendo cookie para a chave: {}", key);
        return Optional.ofNullable(request.getCookies())
        .flatMap(cookies -> Arrays.stream(cookies)
        .filter(cookie -> key.equals(cookie.getName()))
        .findAny()).map(e -> e.getValue())
        .orElse(null);
    }

    public static void deleteCookie(HttpServletResponse response, String nome) {
        LOGGER.info("Deletando cookie para o nome: {}", nome);
        Cookie cookie = new Cookie(nome, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

}
