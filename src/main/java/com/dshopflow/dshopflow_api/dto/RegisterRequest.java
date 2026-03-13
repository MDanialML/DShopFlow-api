package com.dshopflow.dshopflow_api.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String shopName;
    private String email;
    private String password;
}
