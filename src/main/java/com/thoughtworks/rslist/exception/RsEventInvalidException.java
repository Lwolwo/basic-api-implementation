package com.thoughtworks.rslist.exception;

public class RsEventInvalidException extends RuntimeException {
    private String errorMessage;
    public RsEventInvalidException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
