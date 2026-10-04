package com.dshopflow.dshopflow_api.controller;

import com.dshopflow.dshopflow_api.config.SecurityUtils;
import com.dshopflow.dshopflow_api.dto.OrderRequest;
import com.dshopflow.dshopflow_api.model.Order;
import com.dshopflow.dshopflow_api.model.OrderStatus;
import com.dshopflow.dshopflow_api.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final SecurityUtils securityUtils;

    public OrderController(OrderService orderService, SecurityUtils securityUtils) {
        this.orderService = orderService;
        this.securityUtils = securityUtils;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest orderRequest, HttpServletRequest httpRequest) {
        Long shopId = securityUtils.getShopId(httpRequest);
        Order order = orderService.createOrder(orderRequest, shopId);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }


    @PatchMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable long id,
                                              @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }

}
