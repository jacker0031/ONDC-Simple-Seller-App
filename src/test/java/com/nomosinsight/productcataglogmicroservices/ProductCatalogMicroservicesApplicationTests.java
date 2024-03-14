package com.nomosinsight.productcataglogmicroservices;

import com.nomosinsight.productcataglogmicroservices.model.dto.ProductRequest;
import com.nomosinsight.productcataglogmicroservices.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductCatalogMicroservicesApplicationTests {

	@Container
	static MongoDBContainer mongoDBContainer=new MongoDBContainer("mongo:4.4.2");

	@Autowired
	private MockMvc mockMvc;

	private final ObjectMapper objectMapper=new ObjectMapper();

	@Autowired
	private ProductRepository productRepository;

	@DynamicPropertySource
	static  void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
		dynamicPropertyRegistry.add("spring.data.mongodb.uri",mongoDBContainer::getReplicaSetUrl);
	}
	@Test
	void shouldCreateTest() throws Exception {
		ProductRequest productRequest = getProductRequest();
		String productRequestString;
        try {
			productRequestString = objectMapper.writeValueAsString(productRequest);
		} catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productRequestString))
				.andExpect(status().isCreated());
        Assertions.assertEquals(1,productRepository.findAll().size());
	}

	@Test
	void getProduct() throws Exception {
		ProductRequest productRequest = getProductRequest();
		String productRequestString;
		try {
			productRequestString = objectMapper.writeValueAsString(productRequest);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
		mockMvc.perform(MockMvcRequestBuilders.get("/api/product"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json("["+productRequestString+"]"));
	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder().description("iphone 13").name("iphone 13").price(BigDecimal.valueOf(1200)).build();
	}

}
