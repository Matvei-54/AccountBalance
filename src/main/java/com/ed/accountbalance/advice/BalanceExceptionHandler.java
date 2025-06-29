package com.ed.accountbalance.advice;

import com.ed.accountbalance.advice.response.RuntimeExceptionResponse;
import com.ed.accountbalance.exception.ErrorValueIdempotencyKeyException;
import com.ed.accountbalance.exception.balance.BalanceWithNameAlreadyExistsException;
import com.ed.accountbalance.exception.balance.BalanceWithNameNoExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class BalanceExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BalanceWithNameAlreadyExistsException.class)
    private RuntimeExceptionResponse balanceWithNumberAlreadyExists(BalanceWithNameAlreadyExistsException e){
        return new RuntimeExceptionResponse(e.getMessage(), LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BalanceWithNameNoExistsException.class)
    private RuntimeExceptionResponse balanceWithNumberNoExists(BalanceWithNameNoExistsException e){
        return new RuntimeExceptionResponse(e.getMessage(), LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ErrorValueIdempotencyKeyException.class)
    private RuntimeExceptionResponse errorValueIdempotencyKey(ErrorValueIdempotencyKeyException e){
        return new RuntimeExceptionResponse(e.getMessage(), LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private Map<String, String> error(MethodArgumentNotValidException exception){
        Map<String,String> errorValid = new HashMap<>();

        exception.getBindingResult().getAllErrors().forEach(
                errors -> {
                    String field = ((FieldError)errors).getField();
                    String errorMessage = errors.getDefaultMessage();
                    errorValid.put(field, errorMessage);
                }
        );
        return errorValid;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    private RuntimeExceptionResponse error(RuntimeException e){
        return new RuntimeExceptionResponse(e.getMessage(), LocalDateTime.now());
    }


}
