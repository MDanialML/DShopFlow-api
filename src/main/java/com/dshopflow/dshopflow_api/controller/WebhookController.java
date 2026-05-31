package com.dshopflow.dshopflow_api.controller;


import com.dshopflow.dshopflow_api.model.Order;
import com.dshopflow.dshopflow_api.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {

    private final PaymentService paymentService;

    public WebhookController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    //Real webhook endpoint - Stripe calls this
    @PostMapping("/stripe")
    public ResponseEntity<String> stripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature")
            String sigHeader){
        //Signature verification goes here
        //when real Stripe is integerated
        return ResponseEntity.ok("received");
    }

    //Mock webhook trigger - You call this during testing
    @GetMapping("/mock-success/{sessionId}")
    public ResponseEntity<Order> mockSuccess(@PathVariable String sessionId){
        Order order = paymentService
                .handlePaymentSuccess(sessionId);
        return ResponseEntity.ok(order);
    }
}
