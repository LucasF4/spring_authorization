package com.example.apiFinanceiro.apiFinanceiro.services.infra;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.apiFinanceiro.apiFinanceiro.entities.User;
import com.example.apiFinanceiro.apiFinanceiro.services.Repositories.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenservice;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        var login = this.tokenservice.validateToken(token);
        System.out.println(login);
        System.out.println(token);
        if(login != null){
            System.out.printf("Meu login: %s",login);
            User user = userRepository.findByEmail(login).orElseThrow(() -> new RuntimeException("Não Encontrado"));
            var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } 
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){

        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;

        System.out.println(authHeader);

        return authHeader.replace("Bearer ", "");
    }
}
