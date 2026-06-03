package com.dshopflow.dshopflow_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InventoryUpdateMessage {
    private Long productId;
    private String productName;
    private Integer newStockQty;
    private Integer lowStockThreshold;
    private boolean isLowStock;
}
