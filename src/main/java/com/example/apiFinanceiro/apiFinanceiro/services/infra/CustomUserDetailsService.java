package com.example.apiFinanceiro.apiFinanceiro.services.infra;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.apiFinanceiro.apiFinanceiro.entities.User;
import com.example.apiFinanceiro.apiFinanceiro.services.Repositories.UserRepository;


@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Usuário não Encontrado."));  
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPswd(), new ArrayList<>()); 
    }
    
    
}
