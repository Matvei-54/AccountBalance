package com.ed.accountbalance.exception;

public class ErrorValueIdempotencyKeyException extends RuntimeException {

    public ErrorValueIdempotencyKeyException() {
        super("Incorrect idempotency key");
    }
}
