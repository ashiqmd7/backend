package com.G2T5203.wingit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.zip.InflaterOutputStream;

@SpringBootApplication
public class WingitApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(WingitApplication.class, args);

		try {
			org.springframework.core.io.Resource resource = new ClassPathResource("application.properties");
			Properties props = PropertiesLoaderUtils.loadProperties(resource);
			String activeProfile = props.getProperty("spring.profiles.active");
			boolean notProdProfile = !activeProfile.equals("prod");
			if (notProdProfile) {
				DatabaseInitializer.init(context);
			}
		} catch (IOException e) {
			System.out.println("ERROR: " + e.getLocalizedMessage());
		}
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
