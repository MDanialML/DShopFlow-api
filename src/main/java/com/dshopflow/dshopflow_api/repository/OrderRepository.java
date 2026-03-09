package com.dshopflow.dshopflow_api.repository;

import com.dshopflow.dshopflow_api.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
