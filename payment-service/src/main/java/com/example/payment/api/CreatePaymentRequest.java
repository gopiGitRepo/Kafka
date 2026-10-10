package com.example.payment.api;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record CreatePaymentRequest(
    @NotBlank @Size(max = 100) String requestId,
    @NotBlank @Size(max = 100) String customerId,
    @NotNull @DecimalMin(value = "0.01") BigDecimal amount,
    @NotBlank @Pattern(regexp = "[A-Z]{3}", message = "currency must be a 3-letter uppercase ISO-style code") String currency
) {}
