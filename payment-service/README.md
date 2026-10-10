# Payment Service (demo starter)

Java 17 + Spring Boot 4.1.1 REST API that stores payment requests in H2 and publishes `PAYMENT_REQUESTED` JSON events to Kafka. This is a local development starter, **not a real payment processor**: it does not charge cards, connect to a bank/payment gateway, or provide production-grade financial guarantees.

## Prerequisites
- JDK 20+
- Maven 3.6.3+
- Kafka broker reachable at `localhost:9092` (or set `KAFKA_BOOTSTRAP_SERVERS`)

## Run~~~~
```bash
mvn clean test
mvn spring-boot:run
```
The service starts on `http://localhost:8081`. The `payment-events` topic is created with one partition and one replica for local development.

## Create payment request
```bash
curl -i -X POST http://localhost:8081/api/v1/payments \
  -H "Content-Type: application/json" \
  -d '{"requestId":"req-10001","customerId":"cust-123","amount":125.50,"currency":"INR"}'
```

`requestId` is the idempotency key. Repeating a request with the same key returns the existing payment rather than intentionally creating a second payment. Use the same key only for the same logical request; this starter does not compare changed payloads for an existing key.

## Get payment
```bash
curl http://localhost:8081/api/v1/payments/<paymentId>
```

## Health
```bash
curl http://localhost:8081/actuator/health
```

## Example Kafka event
```json
{
  "eventId":"a-unique-event-id",
  "eventType":"PAYMENT_REQUESTED",
  "paymentId":"payment-uuid",
  "requestId":"req-10001",
  "customerId":"cust-123",
  "amount":125.50,
  "currency":"INR",
  "status":"ACCEPTED",
  "occurredAt":"2026-10-10T10:00:00Z"
}
```

## Notes before production
- DB persistence and Kafka publication are not atomic in this starter. Implement a transactional outbox and retry worker before relying on this for financial workflows.
- Add authentication/authorization, rate limits, audit policy, secrets management, schema/version strategy, DLQ/retry policy, metrics, and integration tests.
- Do not log card numbers, CVV, credentials, or other payment secrets. This API deliberately does not accept card data.
- Status currently describes request acceptance/event publication, not successful settlement. A downstream consumer can publish a separate payment outcome event and the service can then update status.
