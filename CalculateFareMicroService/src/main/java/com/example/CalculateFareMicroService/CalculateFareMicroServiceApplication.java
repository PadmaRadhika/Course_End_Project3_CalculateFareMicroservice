package com.example.CalculateFareMicroService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
@EnableDiscoveryClient
@SpringBootApplication
public class CalculateFareMicroServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalculateFareMicroServiceApplication.class, args);
	}

}
