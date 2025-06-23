package com.ed.accountbalance.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record BalanceDtoCreateRequest(
        @NotNull
        @NotBlank(message = "Account Name is required")
        String accountName,

        @NotNull
        @NotBlank(message = "Idempotency Key is required")
        String idempotencyKey) {
}
