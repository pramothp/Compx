package com.compx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.compx"})
public class CompxApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompxApplication.class, args);
	}

}
