package com.mdsl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PrimeProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrimeProductApplication.class, args);
	}

}
