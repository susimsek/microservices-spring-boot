package com.eureka.gallery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient    //  eureka clienti enable eder.
@EnableCircuitBreaker    // circuit breakersi enable eder.
public class SpringEurekaGalleryApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringEurekaGalleryApplication.class, args);
	}

}

