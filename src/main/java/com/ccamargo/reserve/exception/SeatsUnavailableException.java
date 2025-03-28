package com.ccamargo.reserve.exception;

public class SeatsUnavailableException extends RuntimeException {

    public SeatsUnavailableException(String message) {
        super(message);
    }
}
