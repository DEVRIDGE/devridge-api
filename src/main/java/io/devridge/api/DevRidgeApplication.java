package io.devridge.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DevRidgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevRidgeApplication.class, args);
	}

}
