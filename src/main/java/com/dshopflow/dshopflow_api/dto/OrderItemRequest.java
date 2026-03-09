package com.dshopflow.dshopflow_api.dto;


import lombok.Data;

@Data
public class OrderItemRequest {
    private long productId;
    private Integer quantity;
}
