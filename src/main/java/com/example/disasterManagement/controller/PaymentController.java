package com.example.disasterManagement.controller;

import com.razorpay.RazorpayException;
import com.example.disasterManagement.service.RazorpayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private RazorpayService razorpayService;

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestParam("amount") int amount, @RequestParam("currency") String currency) {
        try {
            // Check if Razorpay is configured
            if (!razorpayService.isConfigured()) {
                Map<String, Object> demoResponse = new HashMap<>();
                demoResponse.put("status", "demo_mode");
                demoResponse.put("message", "Payment processing is disabled in demo mode.");
                demoResponse.put("amount", amount);
                demoResponse.put("currency", currency);
                return ResponseEntity.ok(demoResponse);
            }
            return ResponseEntity.ok(razorpayService.createOrder(amount, currency, "receipt_" + System.currentTimeMillis()));
        } catch (RazorpayException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Payment processing failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (IllegalStateException e) {
            Map<String, Object> demoResponse = new HashMap<>();
            demoResponse.put("status", "demo_mode");
            demoResponse.put("message", e.getMessage());
            return ResponseEntity.ok(demoResponse);
        }
    }

}