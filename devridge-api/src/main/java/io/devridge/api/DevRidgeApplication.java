package io.devridge.api;

import io.devridge.core.domain.CoreDomainConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import({CoreDomainConfig.class})
@SpringBootApplication(scanBasePackages = {"io.devridge.api", "io.devridge.admin"})
public class DevRidgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevRidgeApplication.class, args);
	}
}
