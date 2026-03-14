package com.dshopflow.dshopflow_api.repository;

import com.dshopflow.dshopflow_api.model.Order;
import com.dshopflow.dshopflow_api.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByShopId(Long shopId);

    Optional<Order> findByIdAndShopId(Long id, Long shopId);

    List<Order> findByShopIdAndStatus(Long shopId, OrderStatus status);
}
