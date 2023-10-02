package com.teamg.BookBee.service;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class CookieService {

    public static void setCookie(HttpServletResponse response, String key, String valor) {
        Cookie cookie = new Cookie(key, valor);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }


    public static String getCookie(HttpServletRequest request, String key) {
        return Optional.ofNullable(request.getCookies())
        .flatMap(cookies -> Arrays.stream(cookies)
        .filter(cookie -> key.equals(cookie.getName()))
        .findAny()).map(e -> e.getValue())
        .orElse(null);
    }

    public static void deleteCookie(HttpServletResponse response, String nome) {
        Cookie cookie = new Cookie(nome, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

}
