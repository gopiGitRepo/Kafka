package com.example.payment.api;

import com.example.payment.service.PaymentNotFoundException;
import com.example.payment.service.PaymentService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentService service;
    public PaymentController(PaymentService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<PaymentResponse> create(@Valid @RequestBody CreatePaymentRequest request) {
        PaymentResponse response = PaymentResponse.from(service.create(request));
        return ResponseEntity.created(URI.create("/api/v1/payments/" + response.paymentId())).body(response);
    }

    @GetMapping("/{paymentId}")
    public PaymentResponse get(@PathVariable String paymentId) {
        return PaymentResponse.from(service.getById(paymentId));
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    ResponseEntity<Map<String, String>> notFound(PaymentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "PAYMENT_NOT_FOUND", "message", ex.getMessage()));
    }
}
