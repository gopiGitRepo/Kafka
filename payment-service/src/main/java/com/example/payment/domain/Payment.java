package com.example.payment.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payments", uniqueConstraints = @UniqueConstraint(name = "uk_payment_request_id", columnNames = "request_id"))
public class Payment {
    @Id
    @Column(length = 36, nullable = false)
    private String id;
    @Column(name = "request_id", nullable = false, length = 100)
    private String requestId;
    @Column(name = "customer_id", nullable = false, length = 100)
    private String customerId;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;
    @Column(nullable = false, length = 3)
    private String currency;
    @Column(nullable = false, length = 30)
    private String status;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected Payment() {}

    public Payment(String id, String requestId, String customerId, BigDecimal amount, String currency) {
        this.id = id; this.requestId = requestId; this.customerId = customerId;
        this.amount = amount; this.currency = currency; this.status = PaymentStatus.ACCEPTED.name();
        this.createdAt = Instant.now();
    }
    public String getId() { return id; }
    public String getRequestId() { return requestId; }
    public String getCustomerId() { return customerId; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public void markPublishFailed() { this.status = PaymentStatus.PUBLISH_FAILED.name(); }
}
