package com.example.ozzy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OzzyApplication {

	public static void main(String[] args) {
		System.out.println("OzzyApplication.main start");
		SpringApplication.run(OzzyApplication.class, args);
	}

}
