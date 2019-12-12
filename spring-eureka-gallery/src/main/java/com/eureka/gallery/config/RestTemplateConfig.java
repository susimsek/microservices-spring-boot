package com.eureka.gallery.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
class RestTemplateConfig {
	
	// Servisi çağırabilmek için rest template beanı oluştu.
	@Bean
	@LoadBalanced        // Farklı portlarda çalışan servis instancelerini load balancing yapar.
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}