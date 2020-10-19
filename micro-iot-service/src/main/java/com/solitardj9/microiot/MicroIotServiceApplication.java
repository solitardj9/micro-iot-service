package com.solitardj9.microiot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MicroIotServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroIotServiceApplication.class, args);
	}
}