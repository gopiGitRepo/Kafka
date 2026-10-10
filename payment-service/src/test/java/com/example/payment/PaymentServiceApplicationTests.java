package com.example.payment;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
    "spring.kafka.bootstrap-servers=localhost:9092",
    "spring.datasource.url=jdbc:h2:mem:paymenttest;DB_CLOSE_DELAY=-1"
})
class PaymentServiceApplicationTests {
    @Test void contextLoads() {}
}
