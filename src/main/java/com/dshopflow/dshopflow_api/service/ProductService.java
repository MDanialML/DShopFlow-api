package com.dshopflow.dshopflow_api.service;

import com.dshopflow.dshopflow_api.model.Product;
import com.dshopflow.dshopflow_api.model.Shop;
import com.dshopflow.dshopflow_api.repository.ProductRepository;
import com.dshopflow.dshopflow_api.repository.ShopRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;
    ProductService(ProductRepository productRepository, ShopRepository shopRepository   ) {
        this.productRepository = productRepository;
        this.shopRepository = shopRepository;
    }

    public List<Product> getAllProducts(Long  shopId) {
        return productRepository
                .findByShopIdAndIsActiveTrue(shopId);
    }

    public Product getProductById(Long id, Long  shopId) {
        return productRepository.findByIdAndShopId(id, shopId)
                .orElseThrow(() -> new RuntimeException("Product with id " + id + " not found"));
    }

    public  Product createProduct(Product product, Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop with id " + shopId + " not found"));
        product.setShop(shop);
        product.setCreatedAt(LocalDateTime.now());
        product.setIsActive(true);
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product updatedProduct, Long shopId) {
        Product existingProduct = getProductById(id, shopId);
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setStockQty(updatedProduct.getStockQty());
        existingProduct.setSku(updatedProduct.getSku());
        existingProduct.setLowStockThreshold(updatedProduct.getLowStockThreshold());
        return productRepository.save(existingProduct);
    }

    public void deleteProductById(Long id,  Long shopId) {
        productRepository.deleteById(id);
        Product product = getProductById(id, shopId);
        product.setIsActive(false);
        productRepository.save(product);
    }
}
