package com.ed.accountbalance.exception.transaction;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException{
    public InsufficientFundsException(BigDecimal amount){
        super(String.format("Insufficient funds in account balance: %s", amount));
    }
}
