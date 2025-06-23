package com.ed.accountbalance.model.enums;

import lombok.Getter;

@Getter
public enum TransactionType {

    DEPOSIT, TRANSFER, WITHDRAWAL;

    public static TransactionType fromString(String transactionType) {
        for (TransactionType type : TransactionType.values()) {
            if(type.toString().equals(transactionType)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid transaction type: " + transactionType);
    }
}
