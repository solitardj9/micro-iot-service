package com.solitardj9.microiot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class MicroIotDiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroIotDiscoveryApplication.class, args);
	}
}