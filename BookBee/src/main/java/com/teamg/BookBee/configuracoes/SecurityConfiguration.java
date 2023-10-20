package com.teamg.BookBee.configuracoes;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    
    @Autowired
    private SecurityFilter securityFilter;

    List<RequestMatcher> matchers = Arrays.asList(
        new AntPathRequestMatcher("/"),
        new AntPathRequestMatcher("/login"),
        new AntPathRequestMatcher("/logar"),
        new AntPathRequestMatcher("/cadastro"),
        new AntPathRequestMatcher("/cadastra"),
        new AntPathRequestMatcher("/css/**"),
        new AntPathRequestMatcher("/js/**"),
        new AntPathRequestMatcher("/img/**")
    );
    
    List<RequestMatcher> matchersDoc = Arrays.asList(
        new AntPathRequestMatcher("/api-docs"),
        new AntPathRequestMatcher("/api-docs/swagger-config"),
        new AntPathRequestMatcher("/swagger-custom"),
        new AntPathRequestMatcher("/swagger-ui/**"),
        new AntPathRequestMatcher("/swagger-ui.html")
    );

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        RequestMatcher mvcMATcher = new MvcRequestMatcher(null, "/livros/**");

        return httpSecurity
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(matchers.toArray(new RequestMatcher[0])).permitAll()
            .requestMatchers(mvcMATcher).authenticated().anyRequest().authenticated()
            )
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
