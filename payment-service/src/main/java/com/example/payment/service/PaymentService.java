package com.example.payment.service;

import com.example.payment.api.CreatePaymentRequest;
import com.example.payment.api.PaymentPublishException;
import com.example.payment.domain.Payment;
import com.example.payment.repository.PaymentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {
    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final String topic;

    public PaymentService(PaymentRepository repository, KafkaTemplate<String, String> kafkaTemplate,
                          ObjectMapper objectMapper, @Value("${app.kafka.payment-topic}") String topic) {
        this.repository = repository; this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper; this.topic = topic;
    }

    @Transactional(noRollbackFor = PaymentPublishException.class)
    public Payment create(CreatePaymentRequest request) {
        var existing = repository.findByRequestId(request.requestId());
        if (existing.isPresent()) return existing.get();

        Payment payment = new Payment(UUID.randomUUID().toString(), request.requestId(), request.customerId(),
                request.amount(), request.currency());
        payment = repository.saveAndFlush(payment);
        PaymentEvent event = new PaymentEvent(UUID.randomUUID().toString(), "PAYMENT_REQUESTED", payment.getId(),
                payment.getRequestId(), payment.getCustomerId(), payment.getAmount(), payment.getCurrency(),
                payment.getStatus(), payment.getCreatedAt());
        try {
            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(topic, payment.getId(), payload).get(10, TimeUnit.SECONDS);
            log.info("Payment accepted and event published: paymentId={}, requestId={}", payment.getId(), payment.getRequestId());
            return payment;
        } catch (JsonProcessingException | java.util.concurrent.ExecutionException |
                 java.util.concurrent.TimeoutException | InterruptedException | RuntimeException ex) {
            if (ex instanceof InterruptedException) Thread.currentThread().interrupt();
            payment.markPublishFailed();
            repository.save(payment);
            log.error("Failed to publish payment event: paymentId={}", payment.getId(), ex);
            throw new PaymentPublishException(ex);
        }
    }

    @Transactional(readOnly = true)
    public Payment getById(String paymentId) {
        return repository.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException(paymentId));
    }
}
