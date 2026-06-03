package com.dshopflow.dshopflow_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LowStockAlertMessage {
    private Long productId;
    private String productName;
    private Integer currentStock;
    private Integer threshold;
}
