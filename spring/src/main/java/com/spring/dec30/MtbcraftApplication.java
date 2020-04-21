package com.spring.dec30;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@SpringBootApplication
public class MtbcraftApplication {

	public static void main(String[] args) {
		SpringApplication.run(MtbcraftApplication.class, args);
		
		
	}
	@Bean
	public ViewResolver viewResolver() {
			ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
			templateResolver.setTemplateMode("XHTML");
			templateResolver.setPrefix("templates/");;
			templateResolver.setSuffix(".html");
			SpringTemplateEngine engine = new SpringTemplateEngine();
			ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
			viewResolver.setTemplateEngine(engine);
		return viewResolver;
		
	}

}
