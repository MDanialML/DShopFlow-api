package com.dshopflow.dshopflow_api.config;


import com.dshopflow.dshopflow_api.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
    private final JwtService jwtService;

    public SecurityUtils(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public Long getShopId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new RuntimeException("No Authorization header ");
        }

        Long shopId = jwtService.extractShopId(token);
        if (shopId == null){
            throw new RuntimeException("ShopId not found in token");
        }
        return  shopId;
    }

    public String getRole(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        return jwtService.extractRole(token);
    }
}
