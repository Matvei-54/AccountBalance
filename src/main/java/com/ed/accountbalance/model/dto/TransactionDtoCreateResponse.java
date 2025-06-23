package com.ed.accountbalance.model.dto;

import java.math.BigDecimal;

public record TransactionDtoCreateResponse(BigDecimal amount,
                                           String currency,
                                           String transactionType,
                                           String transactionStatus) {
}
