package com.ed.accountbalance.model.enums;

import lombok.Getter;

@Getter
public enum TransactionStatus {

    PROCESSING,
    SUCCESS,
    FAIL;

    public static TransactionStatus fromString(String transactionStatus) {
        for (TransactionStatus status : TransactionStatus.values()) {
            if(status.toString().equals(transactionStatus)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid transaction status: " + transactionStatus);
    }
}
