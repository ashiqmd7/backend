package com.G2T5203.wingit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class WingitApplication {

	public static void main(String[] args) {
		SpringApplication.run(WingitApplication.class, args);
	}

	// create a CORS mapping
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedMethods("*")
						.allowedOrigins("*"
//								"http://localhost:3000",
//								"http://wingit.world",
//								"https://wingit.world",
//								"http://46.17.172.31",
//								"https://46.17.172.31"
						) ;
			}
		};
	}

}
