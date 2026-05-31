package com.dshopflow.dshopflow_api.service;


import com.dshopflow.dshopflow_api.dto.CheckoutRequest;
import com.dshopflow.dshopflow_api.dto.CheckoutResponse;
import com.dshopflow.dshopflow_api.model.Order;
import com.dshopflow.dshopflow_api.model.OrderStatus;
import com.dshopflow.dshopflow_api.model.PaymentStatus;
import com.dshopflow.dshopflow_api.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {

    private final OrderRepository orderRepository;

    public PaymentService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public CheckoutResponse createCheckout(
            CheckoutRequest request,
            Long shopId
    ){
        //Step 1- fetch order scoped to this shop
        Order order = orderRepository
                .findByIdAndShopId(request.getOrderId(), shopId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        //validate before proceeding
        if(order.getPaymentStatus() == PaymentStatus.PAID) {
            throw new RuntimeException("Order is already paid");
        }

        if(order.getOrderItems() == null || order.getOrderItems().isEmpty()){
            throw new RuntimeException("Order has no order items");
        }

        //generate mock session
        String sessionId = "cs_mock" + UUID.randomUUID().toString().replace("-", "");

        String checkoutUrl = "http://localhost:8080/api/payments"+"/mock-checkout" + sessionId;

        //save session ID to order
        order.setStripeSessionId(sessionId);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        //return response
        return new CheckoutResponse(checkoutUrl, sessionId);
    }

    @Transactional
    public Order handlePaymentSuccess(
            String sessionId
    ){
        //step 1 find order by session ID
        Order order = orderRepository
                .findByStripeSessionId(sessionId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        //step 2 - idempotency check
        if(order.getPaymentStatus() == PaymentStatus.PAID) {
            return order;
        }

        //step 3 update order
        order.setPaymentStatus(PaymentStatus.PAID);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setUpdatedAt(LocalDateTime.now());
         return orderRepository.save(order);
    }

}
