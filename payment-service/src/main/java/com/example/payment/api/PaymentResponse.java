package com.example.payment.api;

import com.example.payment.domain.Payment;
import java.math.BigDecimal;
import java.time.Instant;

public record PaymentResponse(String paymentId, String requestId, String customerId,
                              BigDecimal amount, String currency, String status, Instant createdAt) {
    public static PaymentResponse from(Payment p) {
        return new PaymentResponse(p.getId(), p.getRequestId(), p.getCustomerId(), p.getAmount(),
                p.getCurrency(), p.getStatus(), p.getCreatedAt());
    }
}
