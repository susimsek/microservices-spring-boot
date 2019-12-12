package com.eureka.image;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient    //  eureka clienti enable eder. @EnableDiscoveryClient inherit alır
public class SpringEurekaImageApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringEurekaImageApplication.class, args);
    }

}
