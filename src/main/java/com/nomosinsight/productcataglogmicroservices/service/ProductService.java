package com.nomosinsight.productcataglogmicroservices.service;

import com.nomosinsight.productcataglogmicroservices.model.dto.ProductRequest;
import com.nomosinsight.productcataglogmicroservices.model.dto.ProductResponse;
import com.nomosinsight.productcataglogmicroservices.model.entity.Product;
import com.nomosinsight.productcataglogmicroservices.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest){
        Product product=Product.builder()
                .price(productRequest.getPrice())
                .description(productRequest.getDescription())
                .name(productRequest.getName())
                .build();

        Product savedProduct=productRepository.save(product);
        log.info("Creating Product With id {}",savedProduct.getId());
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products=productRepository.findAll();
        return products.stream().map(product -> ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice()).build()).collect(Collectors.toList());
    }
}
