package com.eureka.zuul.config;

import com.eureka.zuul.security.JwtConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfigBean {

    @Bean
    public JwtConfig jwtConfig() {
        return new JwtConfig();
    }
}
