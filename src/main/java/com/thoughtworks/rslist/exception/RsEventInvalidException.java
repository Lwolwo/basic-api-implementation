package com.thoughtworks.rslist.exception;

import lombok.*;

public class RsEventInvalidException extends RuntimeException {
    private String errorMessage;

    public RsEventInvalidException(String errorMessage) {
        super();
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return this.errorMessage;
    }
}
