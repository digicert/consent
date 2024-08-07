package com.digicert.consent.service;

import com.digicert.consent.config.ProductConfig;
import com.digicert.consent.config.initializer.CustomInitializer;
import com.digicert.consent.config.ProductModel;
import com.digicert.consent.entities.ProductEntity;
import com.digicert.consent.repositories.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements CustomInitializer {


    private List<ProductModel> products;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public ProductService(ProductConfig productConfig, ProductRepository productRepository) {
        this.objectMapper = new ObjectMapper(new YAMLFactory());
        this.products = productConfig.getProducts();
        this.productRepository = productRepository;
    }

    @Override
    public void init() {
        try {
            createOrUpdateProduct();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createOrUpdateProduct() throws IOException {
        Resource resource = new ClassPathResource("consent/products.yml");
        String yaml = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        ProductConfig newConfig = objectMapper.readValue(yaml, ProductConfig.class);
        if (newConfig.getProducts() != null) {
            products = newConfig.getProducts();
        }
        if (products != null && !products.isEmpty()) {
            for (ProductModel product : products) {
                Optional<ProductEntity> existingProducts = productRepository.findByName(product.getName());
                if (existingProducts.isPresent()) {
                    existingProducts.get().setName(product.getName());
                    productRepository.save(existingProducts.get());
                } else {
                    ProductEntity productEntity = new ProductEntity();
                    productEntity.setName(product.getName());
                    productRepository.save(productEntity);
                }
            }
        }

    }

}
