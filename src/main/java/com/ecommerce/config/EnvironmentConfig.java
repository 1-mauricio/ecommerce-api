package com.ecommerce.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class EnvironmentConfig {
    
    @Bean
    public CommandLineRunner validateEnvironment(Environment env) {
        return args -> {
            System.out.println("Sistema de E-commerce iniciando...");
            System.out.println("Perfil ativo: " + (env.getActiveProfiles().length > 0 ? 
                String.join(", ", env.getActiveProfiles()) : "default"));
            System.out.println("Configuração carregada com sucesso!");
        };
    }
} 