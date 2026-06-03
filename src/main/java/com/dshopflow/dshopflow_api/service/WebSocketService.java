package com.dshopflow.dshopflow_api.service;

import com.dshopflow.dshopflow_api.dto.InventoryUpdateMessage;
import com.dshopflow.dshopflow_api.dto.LowStockAlertMessage;
import com.dshopflow.dshopflow_api.dto.NewOrderMessage;
import com.dshopflow.dshopflow_api.model.Order;
import com.dshopflow.dshopflow_api.model.Product;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    private final SimpMessagingTemplate messsagingTemplate;

    public WebSocketService(SimpMessagingTemplate messsagingTemplate) {
        this.messsagingTemplate = messsagingTemplate;
    }

    //send inventory update to shopdashboard
    public void sendInventoryUpdate(Long shopId, Product product){
        String destination = "/topic/shop/" + shopId + "/inventory";

        InventoryUpdateMessage message =
                new InventoryUpdateMessage(
                        product.getId(),
                        product.getName(),
                        product.getStockQty(),
                        product.getLowStockThreshold(),
                        product.getStockQty() <= product.getLowStockThreshold()
                );
        messsagingTemplate.convertAndSend(destination, message);
    }

    // Send new order notification to shop dashboard
    public void sendNewOrder(Long shopId, Order order){
        String destination =
                "/topic/shop/" + shopId + "/order";
        NewOrderMessage message = new NewOrderMessage(
                order.getId(),
                order.getCustomerName(),
                order.getTotalAmount(),
                order.getStatus().name(),
                order.getOrderDate()
        );

        messsagingTemplate.convertAndSend(destination, message);
    }

    // send low stock alert to shop dashboard
    public void sendLowStockAlert(
            Long shopId, Product product
    ){
        String destination = "/topic/shop/" + shopId + "/alerts";

        LowStockAlertMessage message =
                new LowStockAlertMessage(
                        product.getId(),
                        product.getName(),
                        product.getStockQty(),
                        product.getLowStockThreshold()
                );
        messsagingTemplate.convertAndSend(destination, message);
    }
}
