package com.rajat.inventoryservice;

import com.rajat.inventoryservice.model.Inventory;
import com.rajat.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
		return args -> {
			Inventory i1 = Inventory.builder()
					.skuCode("iphone_14")
					.quantity(100)
					.build();

			Inventory i2 = Inventory.builder()
					.skuCode("iphone_14_red")
					.quantity(0)
					.build();

			inventoryRepository.save(i1);
			inventoryRepository.save(i2);
		};
	}
}
