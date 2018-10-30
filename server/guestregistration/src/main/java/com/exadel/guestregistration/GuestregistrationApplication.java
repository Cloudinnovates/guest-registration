package com.exadel.guestregistration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.exadel.guestregistration.repositories.EventRepository;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@ComponentScan("com.exadel.guestregistration")    
public class GuestregistrationApplication {
	
	@Autowired
	private EventRepository eventRepository;       
	
	public static void main(String[] args) {
		SpringApplication.run(GuestregistrationApplication.class, args);       
	}

} 
