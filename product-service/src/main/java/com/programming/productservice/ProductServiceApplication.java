package com.programming.productservice;

import com.programming.productservice.enums.ProductCategory;
import com.programming.productservice.models.Product;
import com.programming.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class ProductServiceApplication {

	@Autowired
	private ProductRepository productRepository;

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner() {
        return args -> {
			Product product = Product.builder()
					.name("Sample Product")
					.description( "This is a sample product description.")
					.price(BigDecimal.valueOf(10000))
					.category(ProductCategory.ELECTRONICS)
					.build();
            productRepository.save(product);
        };
    }

}
