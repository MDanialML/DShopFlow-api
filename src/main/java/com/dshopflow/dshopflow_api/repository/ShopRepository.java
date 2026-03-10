package com.dshopflow.dshopflow_api.repository;

import com.dshopflow.dshopflow_api.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<Shop, Long> {
}
