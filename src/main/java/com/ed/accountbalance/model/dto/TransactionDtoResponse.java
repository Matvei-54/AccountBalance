package com.ed.accountbalance.model.dto;

import java.math.BigDecimal;

public record TransactionDtoResponse(String currency,
                                     BigDecimal amount,
                                     String transactionType,
                                     String transactionStatus,
                                     String balanceName) {
}
