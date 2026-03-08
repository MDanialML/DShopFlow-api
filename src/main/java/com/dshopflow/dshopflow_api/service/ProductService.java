package com.dshopflow.dshopflow_api.service;

import com.dshopflow.dshopflow_api.model.Product;
import com.dshopflow.dshopflow_api.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findByIsActiveTrue();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product with id " + id + " not found"));
    }

    public  Product createProduct(Product product) {
        product.setCreatedAt(LocalDateTime.now());
        product.setIsActive(true);
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = getProductById(id);
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setStockQty(updatedProduct.getStockQty());
        existingProduct.setSku(updatedProduct.getSku());
        existingProduct.setLowStockThreshold(updatedProduct.getLowStockThreshold());
        return productRepository.save(existingProduct);
    }

    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
        Product product = getProductById(id);
        product.setIsActive(false);
        productRepository.save(product);
    }
}
