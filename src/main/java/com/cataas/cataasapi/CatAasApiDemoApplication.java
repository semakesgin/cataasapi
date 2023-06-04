package com.cataas.cataasapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CatAasApiDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatAasApiDemoApplication.class, args);
	}

}
