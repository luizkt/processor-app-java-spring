package br.com.fiap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "br.com.fiap")
@Configuration
@EnableJpaRepositories(basePackages = { "br.com.fiap.repository" })
@EntityScan(basePackages = "br.com.fiap.entity")
public class ProcessorApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProcessorApplication.class, args);
	}
}
