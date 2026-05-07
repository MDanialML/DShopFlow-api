package com.dshopflow.dshopflow_api.controller;

import com.dshopflow.dshopflow_api.config.SecurityUtils;
import com.dshopflow.dshopflow_api.dto.CheckoutRequest;
import com.dshopflow.dshopflow_api.dto.CheckoutResponse;
import com.dshopflow.dshopflow_api.service.PaymentService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    //required classes/dependencies
    private final PaymentService paymentService;
    private final SecurityUtils securityUtils;

    //constructor injection
    public PaymentController(PaymentService paymentService, SecurityUtils securityUtils) {
        this.paymentService = paymentService;
        this.securityUtils = securityUtils;
    }

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> createCheckout(
            @RequestBody CheckoutRequest request,
            HttpServletRequest httpRequest
    ){
        Long shopId = securityUtils.getShopId(httpRequest);
        return ResponseEntity.ok(paymentService.createCheckout(request, shopId));
    }

    @GetMapping("/mock-checkout/{sessionId}")
    public ResponseEntity<String> mockCheckoutPage(
            @PathVariable String sessionId
    ){
        return ResponseEntity.ok(
                "<html><body>" +
                        "<h2>Mock Checkout Page</h2>" +
                        "<p>Session: " + sessionId + "</p>" +
                        "<a href='http://localhost:8080/api/webhooks" +
                        "/mock-success/" + sessionId + "'>" +
                        "Simulate Payment Success</a>" +
                        "</body></html>"
        );
    }


}
