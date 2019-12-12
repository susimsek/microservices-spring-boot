package com.eureka.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableEurekaClient        // Eureka client gibi davranır
@EnableZuulProxy        //  Zuul aktif
public class SpringEurekaZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringEurekaZuulApplication.class, args);
    }

}
