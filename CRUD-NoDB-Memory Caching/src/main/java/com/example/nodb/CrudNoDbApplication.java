package com.example.nodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CrudNoDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudNoDbApplication.class, args);
	}
}
	


