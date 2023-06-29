package com.aip.inventory;

import com.aip.inventory.domain.Inventory;
import com.aip.inventory.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
//		return args -> {
//			Inventory i1 = Inventory.builder()
//					.skuCode("SKU_500")
//					.name("Iphone 13")
//					.quantity(100)
//					.build();
//
//			Inventory i2 = Inventory.builder()
//					.quantity(14)
//					.name("Iphone 14 pro max")
//					.skuCode("SKU_501")
//					.build();
//			inventoryRepository.save(i1);
//			inventoryRepository.save(i2);
//		};
//	}

}
