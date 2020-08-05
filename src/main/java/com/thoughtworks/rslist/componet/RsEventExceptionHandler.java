package com.thoughtworks.rslist.componet;

import com.thoughtworks.rslist.exception.*;
import com.thoughtworks.rslist.exception.Error;
import lombok.extern.log4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;

import java.util.logging.*;

@ControllerAdvice
@Log4j2
public class RsEventExceptionHandler {

    @ExceptionHandler({RsEventInvalidException.class, MethodArgumentNotValidException.class})
    public ResponseEntity RsEventExceptionHandler(Exception e) {
        String errorMessage;
        if (e instanceof RsEventInvalidException) {
            errorMessage = e.getMessage();
        } else {
            errorMessage = "invalid param";
        }
        Error error = new Error();
        error.setError(errorMessage);

        return ResponseEntity.badRequest().body(error);
    }

    public void RsEventErrorHandler(Error e) {
        log.error("error occur!!!");
    }
}
