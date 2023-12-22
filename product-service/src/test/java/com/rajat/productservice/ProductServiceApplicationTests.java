package com.rajat.productservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rajat.productservice.dto.ProductDto;
import com.rajat.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	ProductRepository productRepository;
	@Container
	static PostgreSQLContainer container = new PostgreSQLContainer("postgres:15.2-alpine");

    @DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url",container::getJdbcUrl);
		registry.add("spring.datasource.username",container::getUsername);
		registry.add("spring.datasource.password",container::getPassword);
	}

	@Test
	void shouldCreateProduct() throws Exception {
		ProductDto productDto = getProductRequest();
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(productDto)))
				.andExpect(status().is2xxSuccessful());
		Assertions.assertEquals(1,productRepository.findAll().size());

	}
	@Test
	void shouldGetAllProducts() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
				.get("/api/product")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").exists())
				.andExpect(jsonPath("$[0].name").value("Iphone 14"));
	}
	private ProductDto getProductRequest() {
		return ProductDto.builder()
				.name("Iphone 14")
				.description("test IPhone 14")
				.price(70000L)
				.build();
	}

}
