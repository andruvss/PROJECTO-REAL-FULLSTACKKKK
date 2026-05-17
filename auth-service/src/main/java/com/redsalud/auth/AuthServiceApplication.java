package com.redsalud.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// PasswordEncoder bean is provided in com.redsalud.auth.config.SecurityConfig

@SpringBootApplication
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    // PasswordEncoder bean is defined in SecurityConfig
}