package com.riverex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RiverExApplication {

	public static void main(String[] args) {
		SpringApplication.run(RiverExApplication.class, args);
	}

}

/*
@SpringBootApplication does three jobs at once:

 - It marks this class as the root of the application.
 - It turns on component scanning. Spring looks through the com.riverex package,
  finds classes marked with annotations such as @RestController, and creates those
  objects for you. That is why you never write new HelloController().

 - It turns on auto-configuration, so Spring Boot sets up the web server and the other
libraries on the classpath without you writing that setup code.
*/