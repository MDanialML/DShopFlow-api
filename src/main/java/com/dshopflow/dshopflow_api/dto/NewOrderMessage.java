package com.dshopflow.dshopflow_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NewOrderMessage {

    private Long orderId;
    private String customerName;
    private Double totalAmount;
    private String status;
    private LocalDateTime orderDate;
}
