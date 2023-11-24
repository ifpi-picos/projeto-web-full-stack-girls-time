package com.teamg.BookBee.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
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
        try {
            valor = URLEncoder.encode(valor, "UTF-8"); // Codificar o valor
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Cookie cookie = new Cookie(key, valor);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        LOGGER.info("Configurando cookie para a chave: {} finalizado", key);
    }

    public static String getCookie(HttpServletRequest request, String key) {
        LOGGER.info("Obtendo cookie para a chave: {}", key);
         return Optional.ofNullable(request.getCookies())
        .flatMap(cookies -> Arrays.stream(cookies)
        .filter(cookie -> key.equals(cookie.getName()))
        .findAny()).map(e -> {
            try {
                LOGGER.info("Obtendo cookie para a chave: {} Finalizada", key);
                return URLDecoder.decode(e.getValue(), "UTF-8"); // Decodificar o valor
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
                LOGGER.info("Obtendo cookie para a chave: {} Finalizada", key);
                return null;
            }
        })
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
