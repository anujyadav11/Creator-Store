package com.example.creatorstore.services;

import com.example.creatorstore.entities.Product;
import com.example.creatorstore.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product createProduct( Product product) {
        return productRepository.save(product);
    }
    public Product updateProduct(Long id , Product product) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("product not found with id " + id));

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setStockQuantity(product.getStockQuantity());

        return productRepository.save(existingProduct);
    }
    public List<Product> getProducts() {
        return productRepository.findAll();
    }
    public Product getProductById( Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("product not found with id " + id));
    }
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
