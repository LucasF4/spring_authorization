package com.example.apiFinanceiro.apiFinanceiro.services.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.apiFinanceiro.apiFinanceiro.entities.User;

public interface UserRepository extends JpaRepository<User, String>  {
    Optional<User> findByEmail(String email);
}
