package com.ed.accountbalance.model.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


public record TransactionDtoCreateRequest(
        @NotNull
        @NotBlank(message = "Amount is required")
        @DecimalMin("1.00")
        BigDecimal amount,

        @NotNull
        @NotBlank(message = "Account Name Type is required")
        String accountName,

        @NotNull
        @NotBlank(message = "Idempotency Key is required")
        String idempotencyKey
) {
}
