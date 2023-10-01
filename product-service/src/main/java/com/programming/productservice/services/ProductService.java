package com.programming.productservice.services;

import com.programming.productservice.dtos.ProductRequest;
import com.programming.productservice.dtos.ProductUpdateRequest;
import com.programming.productservice.exceptions.ResourceNotFoundException;
import com.programming.productservice.models.Product;
import com.programming.productservice.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public void create(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .category(productRequest.getProductCategory())
                .build();
        productRepository.save(product);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Product update(UUID id, ProductUpdateRequest productUpdateRequest) {
        try {
            Product product = productRepository.getReferenceById(id);
            updateData(product, productUpdateRequest);

            return productRepository.save(product);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(Product product, ProductUpdateRequest productUpdateRequest) {
        product.setName(productUpdateRequest.getName());
        product.setDescription(productUpdateRequest.getDescription());
        product.setPrice(productUpdateRequest.getPrice());
        product.setCategory(productUpdateRequest.getProductCategory());
    }

    public void delete(UUID id) {
        productRepository.deleteById(id);
    }
}
