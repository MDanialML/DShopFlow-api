package com.dshopflow.dshopflow_api.repository;

import com.dshopflow.dshopflow_api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByShopIdAndIsActiveTrue(Long shopId);

    Optional<Product> findByIdAndShopId(Long id, Long shopId);

    Boolean existsBySkuAndShopId(String sku, Long shopId);

    List<Product> findByShopIdAndIsActiveTrueAndStockQtyLessThanEqual(Long shopId, Integer threshold);

}
