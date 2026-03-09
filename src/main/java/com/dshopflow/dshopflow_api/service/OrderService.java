package com.dshopflow.dshopflow_api.service;

import com.dshopflow.dshopflow_api.dto.OrderItemRequest;
import com.dshopflow.dshopflow_api.dto.OrderRequest;
import com.dshopflow.dshopflow_api.model.*;
import com.dshopflow.dshopflow_api.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.dshopflow.dshopflow_api.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order createOrder(OrderRequest request){
        //validate all product and stock before touching anything
        HashMap<Long, Product> validatedProducts =  new HashMap<>();
        for(OrderItemRequest itemRequest : request.getItems()){
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemRequest.getProductId()));
            if(!product.getIsActive()){
                throw new RuntimeException("Product is not available: " + product.getName());
            }
            if(product.getStockQty() < itemRequest.getQuantity()){
                throw new RuntimeException(
                        "Insufficient stock for: " + product.getName() +
                                ". Available: " + product.getStockQty() +
                                ", Requested: " + itemRequest.getQuantity());
            }
            validatedProducts.put(itemRequest.getProductId(),  product);
        }
        //step 2: all products valid, now build the order
        Order order = new Order();
        order.setCustomerName(request.getCustomerName());
        order.setCustomerEmail(request.getCustomerEmail());
        order.setCustomerAddress(request.getCustomerAddress());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.UNPAID);
        order.setOrderDate(LocalDateTime.now());

        //Step 3 - build order items and deduct stock
        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;
        for(OrderItemRequest itemRequest : request.getItems()){
            Product product = validatedProducts.get(
                    itemRequest.getProductId());

            //deduct stock
            product.setStockQty(product.getStockQty() - itemRequest.getQuantity());
            productRepository.save(product);

            //check low stock threshold
            if (product.getStockQty() <= product.getLowStockThreshold()){
                System.out.println("LOW STOCK ALERT: " + product.getName() + "only " + product.getStockQty() + "units remaining");
            }

            //build order item
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setSubTotal(product.getPrice() * itemRequest.getQuantity());

            totalAmount += orderItem.getSubTotal();
            orderItems.add(orderItem);
        }
        //step 4 - finalize and save
        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id){
        return  orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Order not found: " + id));
    }

    public Order updateOrderStatus(Long id, OrderStatus status){
        Order order = getOrderById(id);
        order.setOrderStatus(status);
        return orderRepository.save(order);
    }

}
