package com.eureka.gallery.controllers;

import com.eureka.gallery.entities.Gallery;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/")
public class HomeController {


	private final RestTemplate restTemplate;

	private final Environment env;
	
	@RequestMapping("/")
	public String home() {
		// This is useful for debugging
		// When having multiple instance of gallery service running at different ports.
		// We load balance among them, and display which instance received the request.
		return "Hello from Gallery Service running at port: " + env.getProperty("local.server.port");
	}

	@HystrixCommand(fallbackMethod = "fallback")
	@RequestMapping("/{id}")
	public Gallery getGallery(@PathVariable final int id) {
		log.info(" gallery objesi oluşturuldu ... ");
		// galery objesi oluşturur
		Gallery gallery = new Gallery(id);
		// resim listesini alır
		@SuppressWarnings("unchecked")    // bir failereyi simüle etmek için image servisinden bir exception fırlatacağız
		List<Object> images = restTemplate.getForObject("http://image-service/images/", List.class);
		gallery.setImages(images);
		log.info("image objesi döndürüldü ... ");
		return gallery;
	}

	// başarısızlık durumunda çağrılacak bir geri dönüş methodu
	public Gallery fallback(int galleryId, Throwable hystrixCommand) {
		return new Gallery(galleryId);
	}
	
	// -------- Admin Alanı --------
	// Rolü admin olan kullanıcılar bu endpointe erişir.
	// Rol tabanlı auth eklenecek.
	@RequestMapping("/admin")
	public String homeAdmin() {
		return "Bu admin endpointi. Galery servisinin çalıştığı port: " + env.getProperty("local.server.port");
	}
}