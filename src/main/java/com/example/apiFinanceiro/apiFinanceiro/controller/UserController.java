package com.example.apiFinanceiro.apiFinanceiro.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.apiFinanceiro.apiFinanceiro.dto.RequestLogin;
import com.example.apiFinanceiro.apiFinanceiro.dto.RequestUser;
import com.example.apiFinanceiro.apiFinanceiro.dto.ResponseLogin;
import com.example.apiFinanceiro.apiFinanceiro.entities.User;
import com.example.apiFinanceiro.apiFinanceiro.services.Repositories.UserRepository;
import com.example.apiFinanceiro.apiFinanceiro.services.infra.TokenService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/create")
    public ResponseEntity Create(@RequestBody RequestUser body){

        Optional<User> user = this.userRepository.findByEmail(body.email());

        if(user.isEmpty()){
            User newuser = new User();
            newuser.setFirstname(body.email());
            newuser.setEmail(body.email());
            newuser.setPswd(passwordEncoder.encode(body.pswd()));
            this.userRepository.save(newuser);

            String token = this.tokenService.generateToken(newuser);

            return ResponseEntity.ok(new ResponseLogin(newuser.getFirstname(), token));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This user already exists.");
    }

    @PostMapping("/login")
    public ResponseEntity Login(@RequestBody RequestLogin body) {
       try{
        User user = this.userRepository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User not found!"));

        if(passwordEncoder.matches(body.pswd(), user.getPswd())){
            System.out.printf("Realizando login");
            String token = this.tokenService.generateToken(user);

            return ResponseEntity.ok(new ResponseLogin(user.getFirstname(), token));
        }

        return ResponseEntity.badRequest().body("Invalid Access Credentials");
       }catch(Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
       }
    }
    
}
