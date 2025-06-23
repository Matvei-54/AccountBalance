package com.ed.accountbalance.exception.balance;

public class BalanceWithNameAlreadyExistsException extends RuntimeException {

    public BalanceWithNameAlreadyExistsException(String name) {
        super(String.format("Balance with name '%s' already exists", name));
    }
}
