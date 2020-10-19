package com.solitardj9.microiot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
public class MicroIotApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroIotApiGatewayApplication.class, args);
	}
}