package com.ed.accountbalance.exception.transaction;

public class UnrecognizedOperationTypeException extends RuntimeException{

    public UnrecognizedOperationTypeException(String transactionType){
        super(String.format("Unrecognized transaction type '%s'", transactionType));
    }
}
