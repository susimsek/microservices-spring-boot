package com.eureka.auth.config;

import com.eureka.auth.security.JwtConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfigBean {

    @Bean
    public JwtConfig jwtConfig() {
        return new JwtConfig();
    }
}