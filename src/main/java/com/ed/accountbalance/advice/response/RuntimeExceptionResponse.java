package com.ed.accountbalance.advice.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
public class RuntimeExceptionResponse {
    private String message;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss", timezone = "Europe/Moscow")
    private LocalDateTime timestamp;

    public RuntimeExceptionResponse(String message, LocalDateTime timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }
}
