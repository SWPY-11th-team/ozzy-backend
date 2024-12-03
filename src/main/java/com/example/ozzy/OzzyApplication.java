package com.example.ozzy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class OzzyApplication {

	public static void main(String[] args) {
		System.out.println("Hello World!");
		System.out.println("Hello World! 222");
		SpringApplication.run(OzzyApplication.class, args);
	}

}
