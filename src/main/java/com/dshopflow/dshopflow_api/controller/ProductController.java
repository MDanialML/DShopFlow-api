package com.dshopflow.dshopflow_api.controller;

import com.dshopflow.dshopflow_api.config.SecurityUtils;
import com.dshopflow.dshopflow_api.model.Product;
import com.dshopflow.dshopflow_api.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final SecurityUtils securityUtils;

    public ProductController(ProductService productService,
                             SecurityUtils securityUtils) {
        this.productService = productService;
        this.securityUtils = securityUtils;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(
            HttpServletRequest request) {
        Long shopId = securityUtils.getShopId(request);
        return ResponseEntity.ok(
                productService.getAllProducts(shopId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(
            @PathVariable Long id,
            HttpServletRequest request) {
        Long shopId = securityUtils.getShopId(request);
        return ResponseEntity.ok(
                productService.getProductById(id, shopId));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(
            @RequestBody Product product,
            HttpServletRequest request) {
        Long shopId = securityUtils.getShopId(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(
                        product, shopId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody Product product,
            HttpServletRequest request) {
        Long shopId = securityUtils.getShopId(request);
        return ResponseEntity.ok(
                productService.updateProduct(
                        id, product, shopId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id,
            HttpServletRequest request) {
        Long shopId = securityUtils.getShopId(request);
        productService.deleteProductById(id, shopId);
        return ResponseEntity.noContent().build();
    }
}