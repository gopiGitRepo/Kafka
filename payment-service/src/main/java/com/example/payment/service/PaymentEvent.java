package com.example.payment.service;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentEvent(String eventId, String eventType, String paymentId, String requestId,
                           String customerId, BigDecimal amount, String currency, String status,
                           Instant occurredAt) {}
