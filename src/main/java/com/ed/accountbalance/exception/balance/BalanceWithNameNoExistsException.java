package com.ed.accountbalance.exception.balance;

public class BalanceWithNameNoExistsException extends RuntimeException{
    public BalanceWithNameNoExistsException(String name){
        super(String.format("Balance with name '%s' does not exist", name));
    }
}
