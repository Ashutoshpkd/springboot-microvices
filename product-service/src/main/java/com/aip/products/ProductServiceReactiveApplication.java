package com.aip.products;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ProductServiceReactiveApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceReactiveApplication.class, args);
	}

}
