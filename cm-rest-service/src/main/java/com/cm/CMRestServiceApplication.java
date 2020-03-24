package com.cm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class CMRestServiceApplication extends SpringBootServletInitializer {
	
	public static void main(String[] args) throws Exception {  
	    SpringApplication.run(CMRestServiceApplication.class, args);  
	}  
	
	@Override 
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CMRestServiceApplication.class);
	}

}
