package com.ed.accountbalance.advice;

import com.ed.accountbalance.advice.response.RuntimeExceptionResponse;
import com.ed.accountbalance.exception.transaction.InsufficientFundsException;
import com.ed.accountbalance.exception.transaction.UnrecognizedOperationTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.time.LocalDateTime;

@RestControllerAdvice
public class TransactionExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InsufficientFundsException.class)
    private RuntimeExceptionResponse insufficientFunds(InsufficientFundsException e){
        return new RuntimeExceptionResponse(e.getMessage(), LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UnrecognizedOperationTypeException.class)
    private RuntimeExceptionResponse unrecognizedOperationType(UnrecognizedOperationTypeException e){
        return new RuntimeExceptionResponse(e.getMessage(), LocalDateTime.now());
    }

}
