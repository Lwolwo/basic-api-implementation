package com.thoughtworks.rslist.componet;

import com.thoughtworks.rslist.exception.*;
import com.thoughtworks.rslist.exception.Error;
import org.springframework.http.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class RsEventExceptionHandler {
    @ExceptionHandler({RsEventIndexInvalidException.class, MethodArgumentNotValidException.class})
    public ResponseEntity RsEventExceptionHandler(Exception e) {
        String errorMessage;
        if (e instanceof RsEventIndexInvalidException) {
            errorMessage = e.getMessage();
        } else {
            errorMessage = "invalid param";
        }
        Error error = new Error();
        error.setError(errorMessage);

        return ResponseEntity.badRequest().body(error);
    }
}
